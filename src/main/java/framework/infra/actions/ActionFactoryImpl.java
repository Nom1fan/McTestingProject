package framework.infra.actions;

import Exceptions.NoSuchClientActionException;
import MessagesToClient.ClientActionType;
import framework.infra.annotations.ClientActionAnno;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Mor on 23/04/2016.
 */
public class ActionFactoryImpl implements ActionFactory {

    private Map<ClientActionType, Class<? extends ClientAction>> actionType2Action = new HashMap<>();

    public ActionFactoryImpl() throws IllegalAccessException, InstantiationException {
        super();
        Reflections reflections = new Reflections();
        Set<Class<? extends ClientAction>> classes = reflections.getSubTypesOf(ClientAction.class);
        for (Class<? extends ClientAction> aClass : classes) {
            if(!Modifier.isAbstract(aClass.getModifiers())) {
                if(aClass.isAnnotationPresent(ClientActionAnno.class)) {
                    ClientActionAnno caAnno = aClass.getAnnotation(ClientActionAnno.class);
                    ClientActionType caType = caAnno.actionType();
                    actionType2Action.put(caType, aClass);
                }
            }
        }
    }

    @Override
    public ClientAction getAction(ClientActionType clientActionType) throws NoSuchClientActionException {
        Class<? extends ClientAction> aClass = actionType2Action.get(clientActionType);
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new NoSuchClientActionException(e.getMessage());
        }
    }
}
