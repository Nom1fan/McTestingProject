package framework.infra;

import framework.infra.config.Config;
import framework.infra.config.ProxyConfigurer;
import framework.infra.config.JavaConfig;
import framework.infra.config.ObjectConfigurator;
import org.reflections.Reflections;
import javax.annotation.PostConstruct;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mor on 13/07/2016.
 */
public class ObjectFactory {
    private static ObjectFactory ourInstance = new ObjectFactory();

    public static ObjectFactory getInstance() {
        return ourInstance;
    }

    private List<ObjectConfigurator> objectConfigurators = new ArrayList<>();
    private List<ProxyConfigurer> proxyConfigurers = new ArrayList<>();

    private Config config = new JavaConfig();
    private Reflections reflections = new Reflections("framework");

    private ObjectFactory() {
        Set<Class<? extends ObjectConfigurator>> classes = reflections.getSubTypesOf(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> aClass : classes) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                try {
                    objectConfigurators.add(aClass.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Set<Class<? extends ProxyConfigurer>> classes1 = reflections.getSubTypesOf(ProxyConfigurer.class);
        for (Class<? extends ProxyConfigurer> aClass : classes1) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                try {
                    proxyConfigurers.add(aClass.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public <T> T createObject(Class<T> type) throws Exception {

        type = resolveImplClass(type);
        T t = type.newInstance();
        configure(t);
        secondPhaseContructor(type, t);
        t = handleProxy(type, t);
        return t;
    }

    private <T> T handleProxy(Class<T> type, T t) {
        for (ProxyConfigurer proxyConfigurer : proxyConfigurers) {
            t = (T) proxyConfigurer.wrapWithProxy(t,type);
        }
        return t;
    }


    private <T> void secondPhaseContructor(Class<T> type, T t) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(t);
            }
        }
    }

    private <T> void configure(T t) throws Exception {
        for (ObjectConfigurator objectConfigurator : objectConfigurators) {
            objectConfigurator.configure(t);
        }
    }


    private <T> Class<T> resolveImplClass(Class<T> type) {
        if (type.isInterface()) {
            Class implClass = config.getImplClass(type);
            if (implClass == null) {
                Set<Class<? extends T>> classes = reflections.getSubTypesOf(type);
                if (classes.size() != 1) {
                    throw new RuntimeException("0 or more than one impl class found for interface " + type + " please bind needed impl in your testing.tests.infra.config");
                }
                type = (Class<T>) classes.iterator().next();
            }else {
                type = implClass;
            }
        }
        return type;
    }
}












