package framework.infra.network;

import MessagesToServer.MessageToServer;
import framework.infra.actions.ActionFactory;
import framework.infra.actions.ActionFactoryImpl;
import framework.infra.actions.ClientAction;
import framework.infra.annotations.InjectByType;
import framework.infra.annotations.InjectLogger;
import framework.infra.annotations.IntegerProperty;
import framework.infra.annotations.StringProperty;
import framework.infra.data.Constants;
import ClientObjects.ConnectionToServer;
import DataObjects.DataKeys;
import DataObjects.SpecialMediaType;
import EventObjects.EventReport;
import FilesManager.FileManager;
import MessagesToClient.MessageToClient;
import MessagesToServer.ServerActionType;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Mor on 14/07/2016.
 */
public class ServerProxyImpl implements ServerProxy {

    @StringProperty(property = "server.host")
    private String host;

    @IntegerProperty(property = "server.logic.port")
    private int logicPort;

    @IntegerProperty(property = "server.storage.port")
    private int storagePort;

    @InjectLogger(name = "MediaCallzTests", folder = "TestLogs")
    private Logger logger;

    @InjectByType
    private ActionFactory actionFactory;

    private final Object _lock = new Object();

    private EventReport _lastEventReport = null;

    private boolean _locked;

    @Override
    public ConnectionToServer openConnection(String host, int port) throws IOException {
        ConnectionToServer conn = new ConnectionToServer(host, port, this);
        conn.openConnection();
        return conn;
    }

    @Override
    public void registerUser(String uid, String token) throws IOException {

        logger.info("Registering [User]:" + uid + ". [Token]:" + token);

        HashMap<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.DEVICE_MODEL, Constants.TEST_DEVICE_MODEL);
        data.put(DataKeys.ANDROID_VERSION, Constants.TEST_ANDROID_VERSION);
        data.put(DataKeys.PUSH_TOKEN, token);
        data.put(DataKeys.SMS_CODE, Constants.TEST_SMS_CODE);

        MessageToServer msgRegister = new MessageToServer(ServerActionType.REGISTER, uid, data);
        ConnectionToServer conn = openConnection(host, logicPort);
        conn.sendToServer(msgRegister);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getLogicPort() {
        return logicPort;
    }

    public void setLogicPort(int logicPort) {
        this.logicPort = logicPort;
    }

    public int getStoragePort() {
        return storagePort;
    }

    @Override
    public void uploadFile(String srcId, String destId, SpecialMediaType specialMediaType, FileManager fileForUpload) throws IOException {
        logger.info("Uploading file from [User]:" + srcId + " to [User]:" + destId);

        HashMap<DataKeys, Object> data = new HashMap<>();

        data.put(DataKeys.APP_VERSION, Constants.TEST_APP_VERSION);
        data.put(DataKeys.SOURCE_ID, srcId);
        data.put(DataKeys.SOURCE_LOCALE, Constants.TEST_LOCALE);
        data.put(DataKeys.DESTINATION_ID, destId);
        data.put(DataKeys.DESTINATION_CONTACT_NAME, Constants.TEST_DEST_NAME);
        data.put(DataKeys.MANAGED_FILE, fileForUpload);
        data.put(DataKeys.MD5, fileForUpload.getMd5());
        data.put(DataKeys.EXTENSION, fileForUpload.getFileExtension());
        data.put(DataKeys.FILE_PATH_ON_SRC_SD, fileForUpload.getFileFullPath());
        data.put(DataKeys.FILE_SIZE, fileForUpload.getFileSize());
        data.put(DataKeys.FILE_TYPE, fileForUpload.getFileType());
        data.put(DataKeys.SPECIAL_MEDIA_TYPE, specialMediaType);
        data.put(DataKeys.SOURCE_WITH_EXTENSION, srcId + "." + fileForUpload.getFileExtension());


        ConnectionToServer conn = openConnection(host, storagePort);
        DataOutputStream dos = null;
        BufferedInputStream bis = null;
        try {
            MessageToServer msgUF = new MessageToServer(ServerActionType.UPLOAD_FILE, srcId, data);
            conn.sendToServer(msgUF);

            logger.info("Initiating file data upload...");

            dos = new DataOutputStream(conn.getClientSocket().getOutputStream());

            FileInputStream fis = new FileInputStream(fileForUpload.getFile());
            bis = new BufferedInputStream(fis);

            byte[] buf = new byte[1024 * 8];
            long fileSize = fileForUpload.getFileSize();
            long bytesToRead = fileSize;
            int bytesRead;
            while (bytesToRead > 0 && (bytesRead = bis.read(buf, 0, (int) Math.min(buf.length, bytesToRead))) != -1) {
                dos.write(buf, 0, bytesRead);
                //String msg = "File upload to:"+td.getDestinationId()+" %.0f%% complete";
                //float percent = (float) (fileSize - bytesToRead) / fileSize * 100;
                //publishProgress(String.format(msg, percent));
                bytesToRead -= bytesRead;
            }
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                conn.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void prepareSmsCode(String userId) throws IOException {

        logger.info("Preparing SMS code for [User]:" + userId);

        ConnectionToServer conn = openConnection(host, logicPort);
        MessageToServer msgGetSms = new MessageToServer(ServerActionType.GET_SMS_CODE_FOR_LOAD_TEST, userId);
        conn.sendToServer(msgGetSms);
    }

    @Override
    public void unregister(String userId, String userToken) throws IOException {

        logger.info("Unregistering [User]:" + userId);

        HashMap<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.PUSH_TOKEN, userToken);

        MessageToServer msgUnregister = new MessageToServer(ServerActionType.UNREGISTER, userId, data);
        ConnectionToServer conn = openConnection(host, logicPort);
        conn.sendToServer(msgUnregister);
    }

    @Override
    public void deleteUser(String userId) throws IOException {

        logger.info("Deleting [User]:" + userId);

        HashMap<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.TABLE, Constants.TABLE_SMS_VERIFICATION);
        data.put(DataKeys.COLUMN, Constants.COL_UID);
        data.put(DataKeys.CONDITION, "=");
        data.put(DataKeys.VALUE, userId);

        MessageToServer msgDelUser = new MessageToServer(ServerActionType.DELETE_FROM_DB, userId, data);
        ConnectionToServer conn = openConnection(host, logicPort);
        conn.sendToServer(msgDelUser);

        data = new HashMap<>();
        data.put(DataKeys.TABLE, Constants.TABLE_USERS);
        data.put(DataKeys.COLUMN, Constants.COL_UID);
        data.put(DataKeys.CONDITION, "=");
        data.put(DataKeys.VALUE, userId);

        msgDelUser = new MessageToServer(ServerActionType.DELETE_FROM_DB, userId, data);
        conn = openConnection(host, logicPort);
        conn.sendToServer(msgDelUser);
    }

    @Override
    public void ping(String srcId) throws IOException {
        MessageToServer msgPing = new MessageToServer(ServerActionType.PING, srcId);
        ConnectionToServer conn = openConnection(host, logicPort);
        conn.sendToServer(msgPing);
    }

    @Override
    public void handleMessageFromServer(MessageToClient msg, ConnectionToServer connectionToServer) {

        try {
            _lastEventReport = null;
            logger.info("[MessageToClient]:" + msg.toString());

            ClientAction clientAction = actionFactory.getAction(msg.getActionType());
            clientAction.setConnectionToServer(connectionToServer);
            _lastEventReport = clientAction.doClientAction(msg.getData());

            if (_lastEventReport != null)
                logger.info("Performed clientAction. [EventReport]:" + _lastEventReport.status());
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Failed to process " + "[ClientActionType]:" + msg.getActionType() + ". [Exception]:" + e.getMessage());

        } finally {

            endTranscation(connectionToServer);
        }
    }

    private void endTranscation(ConnectionToServer connectionToServer) {
        synchronized (_lock) {
            _locked = false;
            _lock.notify();
        }
        closeConnection(connectionToServer);
    }

    @Override
    public void isRegistered(String srcId, String destId) throws IOException {

        HashMap<DataKeys, Object> data = new HashMap<>();
        data.put(DataKeys.DESTINATION_ID, destId);

        MessageToServer msgIsLogin = new MessageToServer(ServerActionType.IS_REGISTERED, srcId, data);
        ConnectionToServer conn = openConnection(host, logicPort);
        conn.sendToServer(msgIsLogin);
    }

    @Override
    public void closeConnection(ConnectionToServer connectionToServer) {

        try {
            connectionToServer.closeConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public EventReport getEventReport() throws InterruptedException {

        _locked = true;

        synchronized (_lock) {

            while (_locked) {
                logger.info("getEventReport waiting for notify...");
                _lock.wait(Constants.WAIT_FOR_RESULT_TIMEOUT);
                _locked = false; // Timeout exceeded. Shouldn't stay stuck here.
            }
        }

        return _lastEventReport;
    }

    @Override
    public void handleDisconnection(ConnectionToServer cts, String msg) {
        logger.severe(msg);
        endTranscation(cts);

    }

}
