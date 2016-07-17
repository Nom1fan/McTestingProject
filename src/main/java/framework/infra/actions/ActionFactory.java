package framework.infra.actions;

import Exceptions.NoSuchClientActionException;
import MessagesToClient.ClientActionType;

/**
 * Created by Mor on 15/07/2016.
 */
public interface ActionFactory {
    ClientAction getAction(ClientActionType clientActionType) throws NoSuchClientActionException;
}
