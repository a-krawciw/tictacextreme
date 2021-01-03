const socket = new WebSocket("ws://"+window.location.host+window.location.pathname+'/ws');


// Connection opened
socket.addEventListener('open', function (event) {
    updateBoard()
    console.log("Connected to websocket")
    setInterval(sendPing, 100)
});

// Listen for messages
socket.addEventListener('message', function (event) {
    updateBoard()
    document.getElementById("serverMessage").innerText = event.data
});

function sendPing(){
    socket.send("ping")
}

function updateBoard() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // Typical action to be performed when the document is ready:
            document.getElementById("gameView").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", window.location.href + "/board", true);
    xhttp.send();
}

function sendMove(row, column) {
    socket.send(row + "," + column)
}

function tearDown(){

}