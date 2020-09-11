package model;

import model.entity.Agent;
import model.entity.Food;
import model.threading.DeltaTimeManager;
import model.threading.RunnableTask;
import model.threading.TickTaskThread;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.websocket.Session;

import java.util.ArrayList;

import static java.lang.System.out;

public class Model implements RunnableTask {
    private static Model ourInstance = new Model();
    public static Model Instance() {
        return ourInstance;
    }

    private final int SERVER_RATE = 16;
    private final GameEngine engine = new GameEngine();
    private final DeltaTimeManager deltaManager = new DeltaTimeManager();
    private final TickTaskThread task = new TickTaskThread(() -> sendMessage(getData().toString()), SERVER_RATE);
    private final DatabaseManager database = new DatabaseManager();

    private Session subscriber;
    private boolean canLaunch;
    private boolean canStop;

    private JSONObject getData() {
        JSONObject json = new JSONObject();
        json.put("agentsMove", getAgentsMove());
        json.put("removedAgents", getRemovedAgents());
        json.put("addedFood", getAddedFood());
        json.put("removedFood", getRemovedFood());
        engine.refreshDifferenceManager();
        return json;
    }
    private JSONObject getServiceInformation() {
        JSONObject json = new JSONObject();
        JSONObject radius = new JSONObject();
        radius.put("agentRadius", GameEngine.getAgentRadius());
        radius.put("foodRadius", GameEngine.getFoodRadius());
        json.put("service", radius);
        return json;
    }
    private JSONObject getAddedAgents() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for(Agent a : engine.getAgents()) {
            JSONObject agentJSON = new JSONObject();
            agentJSON.put("id", a.getId());
            agentJSON.put("x", a.getPosition().x);
            agentJSON.put("y", a.getPosition().y);
            agentJSON.put("color", a.getColor().toString());
            array.add(agentJSON);
        }
        json.put("addedAgents", array);
        return json;
    }
    private JSONArray getAgentsMove() {
        JSONArray agentsData = new JSONArray();
        for(Agent a : engine.getAgents()) {
            JSONObject agentJSON = new JSONObject();
            agentJSON.put("id", a.getId());
            agentJSON.put("x", a.getPosition().x);
            agentJSON.put("y", a.getPosition().y);
            agentJSON.put("delta", deltaManager.getDelta());
            agentJSON.put("energy", a.getEnergy());
            agentsData.add(agentJSON);
        }
        deltaManager.refreshOldTime();
        return agentsData;
    }
    private JSONArray getRemovedAgents() {
        JSONArray removedAgents = new JSONArray();
        for(Agent a : engine.getRemovedAgents()) {
            JSONObject removedJSON = new JSONObject();
            removedJSON.put("id", a.getId());
            removedAgents.add(removedJSON);
        }
        return removedAgents;
    }
    private JSONArray getAddedFood() {
        JSONArray addedFood = new JSONArray();
        for(Food f : engine.getAddedFood()) {
            JSONObject addedJSON = new JSONObject();
            addedJSON.put("id", f.getId());
            addedJSON.put("x", f.getPosition().x);
            addedJSON.put("y", f.getPosition().y);
            addedFood.add(addedJSON);
        }
        return addedFood;
    }
    private JSONArray getRemovedFood() {
        JSONArray removedFood = new JSONArray();
        for(Food f : engine.getRemovedFood()) {
            JSONObject removedJSON = new JSONObject();
            removedJSON.put("id", f.getId());
            removedFood.add(removedJSON);
        }
        return removedFood;
    }

    public void sendMessage(String message) {
        try {
            subscriber.getBasicRemote().sendText(message);
        } catch (Exception ex) {
            out.println("Error on send message: " + ex + " Message: " + message);
        }
    }
    public void setScreenSize(Vector size) {
        engine.setScreenSize(size);
    }
    public void subscribeClient(Session subscriber) {
        if(this.subscriber != null) return;
        this.subscriber = subscriber;
        canLaunch = true;
        canStop = false;
    }
    public void unsubscribeClient() {
        this.subscriber = null;
    }

    public void start() {
        if(canLaunch) {
            canLaunch = false;
            canStop = true;
        }
        else return;
        engine.start();
        sendMessage(getServiceInformation().toString());
        sendMessage(getAddedAgents().toString());
        task.start();
    }
    public void abort() {
        if(canStop) canStop = false;
        else return;
        task.abort();
        database.WriteToDatabaseAsync((ArrayList<Agent>) engine.getAgents().clone());
        engine.abort();
    }
    public void suspend() {
        engine.suspend();
        task.suspend();
    }
    public synchronized void resume() {
        task.resume();
        engine.resume();
    }

    private Model() {}
}
