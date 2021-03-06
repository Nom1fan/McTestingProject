package MessagesToServer;

import DataObjects.DataKeys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mor on 23/04/2016.
 */
public class MessageToServer implements Serializable {

    private static final long SerialVersionUID = 7251596388334440813L;

    protected String _messageInitiaterId;
    protected HashMap<DataKeys, Object> _data;
    protected ServerActionType _serverActionType;

    public MessageToServer(ServerActionType serverActionType, String messegeInitiaterId) {

        _messageInitiaterId = messegeInitiaterId;
        _serverActionType = serverActionType;
    }

    public MessageToServer(ServerActionType serverActionType, String messageInitiaterId, HashMap<DataKeys, Object> data) {

        _messageInitiaterId = messageInitiaterId;
        _data = data;
        _serverActionType = serverActionType;
    }

    public String get_messageInitiaterId() {
        return _messageInitiaterId;
    }

    public Map getData() {
        return _data;
    }

    public ServerActionType getActionType() {
        return _serverActionType;
    }
}
