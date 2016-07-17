package framework.infra.actions;

import DataObjects.DataKeys;
import EventObjects.EventReport;
import MessagesToClient.ClientActionType;
import framework.infra.annotations.ClientActionAnno;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Mor on 27/04/2016.
 */
@ClientActionAnno(actionType = ClientActionType.TRIGGER_EVENT)
public class ClientActionTriggerEvent extends ClientAction {

    public ClientActionTriggerEvent() {
        super(ClientActionType.TRIGGER_EVENT);
    }

    @Override
    public EventReport doClientAction(Map data) throws IOException {

        return (EventReport) data.get(DataKeys.EVENT_REPORT);
    }
}
