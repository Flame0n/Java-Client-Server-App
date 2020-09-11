class SocketManager {
    constructor() {
        this.webSocket = new WebSocket("ws://localhost:8080/CourseWorkJava_war_exploded/app");

        this.webSocket.onmessage = function (modelMessage) {
            const json = JSON.parse(modelMessage.data);
            if(json["agentsMove"]) agentManager.dataBuffer.addDataToQueue(json["agentsMove"]);
            if(json["addedFood"]) foodManager.addedBuffer.addDataToQueue(json["addedFood"]);
            if(json["removedFood"]) foodManager.removedBuffer.addDataToQueue(json["removedFood"]);
            if(json["addedAgents"]) agentManager.createAgents(json["addedAgents"]);
            if(json["removedAgents"]) agentManager.removeAgents(json["removedAgents"]);
            if(json["service"]) {
                agentManager.size = json["service"]["agentRadius"];
                foodManager.size = json["service"]["foodRadius"];
            }
            if(json["action"]) setState(json["action"]);
        };

        this.webSocket.onopen = function () {
            webSocket.send(JSON.stringify(renderEngine.getViewData()).toString());
        };

        this.webSocket.onerror = function (error) {
            alert("Websocket error: " + error)
        };

    }

    send(message) {
        this.webSocket.send(message);
    }
}

