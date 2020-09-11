var startButton = document.getElementById("startButton");
var stopButton = document.getElementById("stopButton");

startButton.onclick = function() {
    var action = createUserAction("start");
    webSocket.send(JSON.stringify(action));
};

stopButton.onclick = function() {
    var action = createUserAction("stop");
    webSocket.send(JSON.stringify(action));
};

function createUserAction(action) {
    var json = {};
    var view = {};
    view.action = action;
    json.userAction = view;
    return json;
}