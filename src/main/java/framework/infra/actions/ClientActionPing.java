package framework.infra.actions;

import EventObjects.EventReport;
import EventObjects.EventType;
import MessagesToClient.ClientActionType;
import framework.infra.annotations.ClientActionAnno;


import java.io.IOException;
import java.util.Map;

/**
 * Created by Mor on 15/07/2016.
 */
@ClientActionAnno(actionType = ClientActionType.PING)
public class ClientActionPing extends ClientAction {
    public ClientActionPing() {
        super(ClientActionType.PING);
    }

    @Override
    public EventReport doClientAction(Map DATA) throws IOException {
        return new EventReport(EventType.PING);
    }
}
