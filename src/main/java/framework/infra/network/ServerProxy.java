package framework.infra.network;

import ClientObjects.ConnectionToServer;
import DataObjects.SpecialMediaType;
import EventObjects.EventReport;
import FilesManager.FileManager;
import MessagesToClient.MessageToClient;

import java.io.IOException;

/**
 * Created by Mor on 14/07/2016.
 */
public interface ServerProxy {

    ConnectionToServer openConnection(String host, int port) throws IOException;
    void registerUser(String uid, String token) throws IOException;
    String getHost();
    int getLogicPort();
    int getStoragePort();
    void handleMessageFromServer(MessageToClient msg, ConnectionToServer connectionToServer);
    void closeConnection(ConnectionToServer connectionToServer);
    EventReport getEventReport() throws InterruptedException;
    void handleDisconnection(ConnectionToServer cts, String msg);
    void uploadFile(String srcId, String destId, SpecialMediaType specialMediaType , FileManager fileForUpload) throws IOException;
    void prepareSmsCode(String userId) throws IOException;
    void unregister(String userId, String userToken) throws IOException;
    void deleteUser(String userId) throws IOException;
    void isRegistered(String srcId, String destId) throws IOException;
    void ping(String srcId) throws IOException;

}
