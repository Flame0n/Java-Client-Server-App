package controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import static java.lang.System.out;

@ServerEndpoint("/app")
public class Server {
    private Controller controller = Controller.Instance();

    @OnOpen
    public void handleOpen(Session session) {
        out.println("Connected: " + session);
        controller.subscribeClient(session);
    }

    @OnClose
    public void handleClose(Session session) {
        out.println("Disconnected: " + session);
        controller.unsubscribeClient();
    }

    @OnError
    public void handleError(Throwable t) {
        out.println("Error: " + t);
    }

    @OnMessage
    public String handleMessage(String message) {
        return controller.processMessage(message);
    }
}
