package ClientObjects;

import framework.infra.network.ServerProxy;
import MessagesToClient.MessageToClient;

public class ConnectionToServer extends AbstractClient {


	private ServerProxy _serverProxy;

	/**
	 * Constructs the client.
	 *
	 * @param host the server's host name.
	 * @param port the port number.
	 */
	public ConnectionToServer(String host, int port, ServerProxy serverProxy) {
		super(host, port);
		_serverProxy = serverProxy;
	}

	@Override
	protected void handleMessageFromServer(Object msg) {

		_serverProxy.handleMessageFromServer((MessageToClient)msg , this);
	}


	@Override
	protected void connectionException(Exception e) {

		String errMsg = "Connection error";
		_serverProxy.handleDisconnection(this, e !=null ? errMsg+":"+e.toString() : errMsg);
	}
}
