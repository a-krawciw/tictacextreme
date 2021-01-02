const socket = new WebSocket("ws://"+window.location.host+window.location.pathname+'/ws');


// Connection opened
socket.addEventListener('open', function (event) {
    console.log("Connected to websocket")
});

// Listen for messages
socket.addEventListener('message', function (event) {
    document.getElementById("gameView").innerText = event.data
    console.log('Message from server ', event.data);
});