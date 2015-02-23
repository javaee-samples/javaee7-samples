var wsUri = "wss://" + document.location.host + "/endpoint-wss/websocket";
//var wsUri = "ws://" + document.location.hostname + ":8080/endpoint-wss/websocket";
console.log("Connecting to " + wsUri);
var websocket = new WebSocket(wsUri);
websocket.onopen = function(evt) { onOpen(evt) };
websocket.onmessage = function(evt) { onMessage(evt) };
websocket.onerror = function(evt) { onError(evt) };

var output = document.getElementById("output");

function echo() {
    console.log("echo: " + myField.value);
    websocket.send(myField.value);
    writeToScreen("SENT (text): " + myField.value);
}

function onOpen() {
    console.log("onOpen");
    writeToScreen("CONNECTED");
}

function onMessage(evt) {
    writeToScreen("RECEIVED (text): " + evt.data);
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}
