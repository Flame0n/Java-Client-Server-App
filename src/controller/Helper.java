package controller;

import model.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static java.lang.System.out;

public class Helper {
    private static JSONParser parser = new JSONParser();

    public static Vector parseFieldSize(JSONObject json) {
        double width = 0;
        double height = 0;
        try {
            JSONObject viewData = (JSONObject)json.get("viewData");
            width = Double.parseDouble( viewData.get("width").toString() );
            height = Double.parseDouble( viewData.get("height").toString() );
        } catch (Exception ex) {}
        return new Vector(width, height);
    }
    public static String sendDebugMessage(String message) {
        JSONObject json = new JSONObject();
        JSONObject debug = new JSONObject();
        debug.put("message", message);
        json.put("debug", debug);
        return json.toString();
    }
    public static String getKey(String message) {
        JSONObject json;
        String key;
        try {
            json = (JSONObject)parser.parse(message);
            key = (String)json.keySet().toArray()[0];
        } catch ( Throwable ex ) {
            out.println("JSON Error: " + ex.toString());
            return controller.Helper.sendDebugMessage("Error parse JSON");
        }
        return key;
    }
    public static JSONObject getJSON(String message) {
        JSONObject json = new JSONObject();
        try {
            json = (JSONObject)parser.parse(message);
        } catch ( Throwable ex ) {
            out.println("JSON Error: " + ex.toString());
        }
        return json;
    }
    public static JSONObject makeAction(String action) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        return json;
    }
}
