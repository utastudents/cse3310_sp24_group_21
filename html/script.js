var connection = null;
var game = null;
var playerName = "";
var inLobby = false;
var id = 0;
var gameid = -1;
var p1turns = 0;
var p2turns = 0;
var p1i, p1j, p2i, p2j = 0;
var count = 0;
var timerInterval;
var timerDisplay = document.getElementById('timerDisplay');
var timerSeconds = 180; // 3 minutes

class Lobby {
    Name = null;
}

class UserEvent {
    PlayerIdx = 0;
    GameId = 0;
    i = 0;
    j = 0;
    playing = false;
    start = false;
}

var serverUrl = "ws://" + window.location.hostname + ":9121";
connection = new WebSocket(serverUrl);

connection.onopen = function (evt) {
    console.log("Connection opened.");
    createTable();
}

connection.onclose = function (evt) {
    console.log("Connection closed.");
    document.getElementById("topMessage").textContent = "Server Offline";
}

connection.onmessage = function (evt) {
    var msg = evt.data;
    console.log("Message received: " + msg);
    const obj = JSON.parse(msg);
    
    if (obj.Game) {
        game = obj.Game;
        if (game.state === "PLAYING") {
            document.getElementById("startButton").style.display = "none";
            document.getElementById("lobby").style.display = "none";
            document.getElementById("game").style.display = "block";
            document.getElementById("gameStatus").textContent = "Game Begins";
            fillGrid(game.cells);
        }
    }

    if (obj.YouAre) {
        id = obj.YouAre === "PLAYERONE" ? 0 : 1;
        gameid = obj.GameId;
    }

    if (obj.PlayerName) {
        playerName = obj.PlayerName;
        document.getElementById("topMessage").textContent = "Hello, " + playerName + "!";
    }

    if (obj.BottomMsg) {
        document.getElementById("bottomMessage").textContent = obj.BottomMsg;
    }

    if (obj.Players) {
        updateLobbyStatus(obj.Players);
    }

    if (obj.start) {
        startGame();
    }

    if (obj.gameWords) {
        updateWordBank(obj);
    }
}

function buttonClick(i, j) {
    // Example logic for button clicks handling two-player turns
}

function fillGrid(cells) {
    // Logic to update the game grid cells based on the server state
}

function createTable() {
    // Logic to create the game table dynamically
}

function showRules() {
    document.getElementById("rules").style.display = "block";
}

function hideRules() {
    document.getElementById("rules").style.display = "none";
}

function updateLobbyStatus(players) {
    // Logic to update the lobby status displaying current players
}

function joinLobby() {
    var name = document.getElementById("name").value.trim();
    if (name) {
        var lobby = new Lobby();
        lobby.Name = name;
        connection.send(JSON.stringify(lobby));
        inLobby = true;
    }
}

function startTimer() {
    timerInterval = setInterval(updateTimer, 1000);
}

function updateTimer() {
    // Timer countdown logic
}

function startGame() {
    // Logic to start the game, hide/show elements, and manage game state
}

function updateWordBank(obj) {
    // Logic to update the word bank displayed to players
}


