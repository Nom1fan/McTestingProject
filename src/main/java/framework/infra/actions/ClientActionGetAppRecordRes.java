package framework.infra.actions;

import EventObjects.EventReport;
import EventObjects.EventType;
import MessagesToClient.ClientActionType;
import framework.infra.annotations.ClientActionAnno;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Mor on 27/04/2016.
 */
@ClientActionAnno(actionType = ClientActionType.GET_APP_RECORD_RES)
public class ClientActionGetAppRecordRes extends ClientAction {

    public ClientActionGetAppRecordRes() {
        super(ClientActionType.GET_APP_RECORD_RES);


    }

    @Override
    public EventReport doClientAction(Map data) throws IOException {

        return new EventReport(EventType.APP_RECORD_RECEIVED, null, data);
    }
}
