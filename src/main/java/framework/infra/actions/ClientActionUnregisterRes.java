package framework.infra.actions;

import DataObjects.DataKeys;
import EventObjects.EventReport;
import EventObjects.EventType;
import MessagesToClient.ClientActionType;
import framework.infra.annotations.ClientActionAnno;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Mor on 27/04/2016.
 */
@ClientActionAnno(actionType = ClientActionType.UNREGISTER_RES)
public class ClientActionUnregisterRes extends ClientAction {

    public ClientActionUnregisterRes() {
        super(ClientActionType.UNREGISTER_RES);
    }

    @Override
    public EventReport doClientAction(Map data) throws IOException {

        boolean isUnregisterSuccess = (boolean) data.get(DataKeys.IS_UNREGISTER_SUCCESS);

        if(isUnregisterSuccess)
            return new EventReport(EventType.UNREGISTER_SUCCESS, null, null);
        else
            return new EventReport(EventType.UNREGISTER_FAILURE, null, null);
    }
}
