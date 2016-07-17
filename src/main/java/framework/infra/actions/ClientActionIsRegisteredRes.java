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
@ClientActionAnno(actionType = ClientActionType.IS_REGISTERED_RES)
public class ClientActionIsRegisteredRes extends ClientAction {


    public ClientActionIsRegisteredRes() {
        super(ClientActionType.IS_REGISTERED_RES);
    }

    @Override
    public EventReport doClientAction(Map data) throws IOException {

        boolean isRegistered = (boolean) data.get(DataKeys.IS_REGISTERED);
        String phone = (String) data.get(DataKeys.DESTINATION_ID);

        String desc;
        if(isRegistered) {
            desc = "User "+phone+" is registered";
            return new EventReport(EventType.USER_REGISTERED_TRUE, desc, phone);
        }
        else {
            desc = "User "+phone+" is unregistered";
            return new EventReport(EventType.USER_REGISTERED_FALSE, desc, phone);
        }
    }
}
