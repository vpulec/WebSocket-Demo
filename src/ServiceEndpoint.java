import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Websocket Endpoint
 * 
 * @author PULEVL
 * 
 */
@ServerEndpoint(value = "/echo")
public class ServiceEndpoint {

	private final static List<Session> sessions = new ArrayList<Session>();

	/**
	 * Client disconnect.
	 * 
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		System.out.println("\nClient disconnected. Session count="
				+ session.getOpenSessions().size());
	}

	/**
	 * Error condition
	 * 
	 * @param t
	 */
	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	/**
	 * Message received.
	 * 
	 * @param message
	 *            message
	 * @param session
	 *            session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("\nOnMessage: " + message);

		for (Session sess : session.getOpenSessions()) {
			if (sess != session && sess.isOpen())
				try {
					sess.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Connection open with a new client.
	 * 
	 * @param session
	 *            session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		session.getBasicRemote().sendText("Welcome.");
		System.out.println("\nNew client connected. Session count="
				+ session.getOpenSessions().size());
		sessions.add(session);
	}
}