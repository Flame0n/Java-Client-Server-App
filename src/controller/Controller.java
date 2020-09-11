package controller;

import model.Model;
import model.entity.IdGenerator;
import org.json.simple.JSONObject;

import javax.websocket.Session;
import static java.lang.System.out;

public class Controller {
    private static Controller ourInstance = new Controller();
    public static Controller Instance() {
        return ourInstance;
    }

    private Model model = Model.Instance();

    public String processMessage(String message) {
        JSONObject json = Helper.getJSON(message);
        out.println("Message here: " + message);
        switch (Helper.getKey(message)) {
            case "viewData":
                return processViewData(json);
            case "userAction":
                return processUserAction(json);
            default:
                return controller.Helper.sendDebugMessage("Server echo");
        }
    }
    public void subscribeClient(Session subscriber) {
        model.subscribeClient(subscriber);
    }
    public void unsubscribeClient() {
        model.abort();
        model.unsubscribeClient();
    }

    private String processViewData(JSONObject json) {
        IdGenerator.Refresh();
        model.setScreenSize(Helper.parseFieldSize(json));
        return controller.Helper.sendDebugMessage("engine is running");
    }
    private String processUserAction(JSONObject json) {
        JSONObject userAction = (JSONObject) json.get("userAction");
        String action = userAction.get("action").toString();
        switch (action) {
            case "start":
                model.start();
                model.sendMessage(Helper.makeAction("start").toString());
                break;
            case "stop":
                model.abort();
                model.sendMessage(Helper.makeAction("abort").toString());
                break;
        }
        return controller.Helper.sendDebugMessage("User action done");
    }

    private Controller() { }
}
