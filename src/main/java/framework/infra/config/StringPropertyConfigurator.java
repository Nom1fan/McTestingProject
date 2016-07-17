package framework.infra.config;

import framework.infra.annotations.IntegerProperty;
import framework.infra.annotations.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Mor on 14/07/2016.
 */
public class StringPropertyConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object o) throws Exception {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(StringProperty.class)) {
                StringProperty anno = field.getAnnotation(StringProperty.class);
                String key = anno.property();

                Properties prop = new Properties();
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("config.properties").getFile());
                InputStream is  = new FileInputStream(file);
                prop.load(is);
                Object val = prop.getProperty(key);

                field.setAccessible(true);
                field.set(o, val);
            }
        }
    }
}
