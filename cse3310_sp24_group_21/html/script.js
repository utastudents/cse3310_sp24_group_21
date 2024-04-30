
var connection = null;
var game = null;
var playerName = "";
var inLobby = false;
var id = 0;
var gameid = -1;
var p1turns = 0;
var p2turns = 0;
var p3turns = 0;
var p4turns = 0;
var p1i, p1j, p2i, p2j, c, d = 0;
var p3i, p3j, p4i, p4j = 0;
var count = 0;
var timerInterval;
var timerDisplay = document.getElementById('timerDisplay');
var timerSeconds = 420; // 7 minutes
var playerPoints = {}; 
var selectedWord = "";
class Chat {
    word = null;
    playeridx = null;
    chatstatus = null;
}
class Lobby {
    Name = null;
    playeridx = null;
    status = null;
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
//var serverUrl = "ws://" + window.location.hostname +":"+ (parseInt(location.port) + 100);
connection = new WebSocket(serverUrl);

connection.onopen = function (evt) {
    console.log("open");
    createTable();
}

connection.onclose = function (evt) {
    console.log("close");
    document.getElementById("topMessage").innerHTML = "Server Offline";
}

connection.onmessage = function (evt) {
    var msg = evt.data;
    console.log("Message received: " + msg);
    const obj = JSON.parse(msg);
    if ('Game' in obj) {
        game = obj.Game;
        if (game.state === "PLAYING") {
            document.getElementById("startButton").style.display = "none";
            document.getElementById("lobby").style.display = "none";
            document.getElementById("game").style.display = "block";
            document.getElementById("gameStatus").innerHTML = "Game Begins";
        }
    }
    if ('YouAre' in obj) {

        if (obj.YouAre == "PLAYERONE") {
            id = 0;
        } else if (obj.YouAre == "PLAYERTWO") {
            id = 1;
        } else if (obj.Youare == "PLAYERTHREE") {
            id = 2;
        } else if (obj.Youare == "PLAYERFOUR") {
            id = 3;
        }
        gameid = obj.GameId;

    }
    if ('Game' in obj) {
        game = obj.Game;
        if (game.state === "PLAYING") {
            document.getElementById("startButton").style.display = "none";
            document.getElementById("lobby").style.display = "none";
            document.getElementById("game").style.display = "block";
            document.getElementById("gameStatus").innerHTML = "Game Begins";
        }
    }

    if ('PlayerName' in obj) {
        playerName = obj.PlayerName;
        document.getElementById("topMessage").innerHTML = "Hello, " + playerName + "!";


        //  updateLobbyStatus(obj.PlayerName);
    }

    if ('BottomMsg' in obj) {
        document.getElementById("bottomMessage").innerHTML = obj.BottomMsg;
    }

    if ('players' in obj) {
        updateLobbyStatus(obj.players);
    }

    if ('start' in obj) {
        if (obj.start == true)
        startGame();
    }

    if ('gameWords' in obj) {
        var bank = document.getElementById("word-bank");

        if (count == 0) {
            for (var i = 0; i < obj.gameWords.length; i++) {
                var word = document.createElement("div");
                word.innerHTML = obj.gameWords[i];
                bank.appendChild(word);
            }
        } else if (count > 0) {
            while (bank.firstChild) {
                bank.removeChild(bank.firstChild);
            }
            for (var i = 0; i < obj.gameWords.length; i++) {
                var word = document.createElement("div");
                word.innerHTML = obj.gameWords[i];
                for (var j = 0; j < obj.foundwords.length; j++) {
                    if (obj.foundwords[j] == obj.gameWords[i])
                        word.style.textDecoration = "line-through";
                } bank.appendChild(word);
            }
        } count++;
    }

    if ('cells' in obj) {
        fillGrid(obj.cells);
        if (gameid == obj.GameId) {
            for (var i = 0; i < 20; i++) {
                for (var j = 0; j < 20; j++) {
                    //show colors to players
                    var v = document.getElementById(i + "," + j);
                    if (obj.Button[i][j] == "PLAYERONE")
                        v.style.backgroundColor = "red";
                    else if (obj.Button[i][j] == "PLAYERTWO")
                        v.style.backgroundColor = "blue";
                    else if (obj.Button[i][j] == "PLAYERTHREE")
                        v.style.backgroundColor = "yellow";
                    else if (obj.Button[i][j] == "PLAYERFOUR")
                        v.style.backgroundColor = "green";
                }
            }
        }
    }

    if ('chat' in obj) {//add
        chatbox(obj.chat);
    }
}
function addChat() {
    var addword = document.getElementById("chat-box").value;
    c = new Chat();
    c.word = addword;
    c.chatstatus = true;
    if (id == 0)
        c.playeridx = "PLAYERONE";
    else if (id == 1)
        c.playeridx = "PLAYERTWO";
    else if (id == 2)
        c.playeridx = "PLAYERTHREE";
    else if (id == 3)
        c.playeridx = "PLAYERFOUR";
    connection.send(JSON.stringify(c));
}
function chatbox(word) {
        var chat = document.getElementById("chat");
        while (chat.firstChild) {
        chat.removeChild(chat.firstChild);
        }
    for (var i = 0; i < word.length; i++) {
        var chatmsg = document.createElement("div");
        chatmsg.textContent = word[i];
        chatmsg.style.color = "white";
        chat.appendChild(chatmsg);
    }
}


function selectWord(startRow, startCol, endRow, endCol) {
    selectedWord = ""; 
    if (startRow === endRow && startCol === endCol) {   
        selectedWord = document.getElementById(startRow + "," + startCol).innerHTML;
    } else {
        var rowIncrement = startRow === endRow ? 0 : (endRow > startRow ? 1 : -1);
        var colIncrement = startCol === endCol ? 0 : (endCol > startCol ? 1 : -1);
        for (var row = startRow, col = startCol; row !== endRow || col !== endCol; row += rowIncrement, col += colIncrement) {
            selectedWord += document.getElementById(row + "," + col).innerHTML;
        }
        selectedWord += document.getElementById(endRow + "," + endCol).innerHTML;
    }
} 

function updateLeaderboard() {
    var leaderboardList = document.getElementById("leaderboardList");
    leaderboardList.innerHTML = ""; 
    var sortedPlayerPoints = [];
    for (var player in playerPoints) {
        sortedPlayerPoints.push({ player: player, points: playerPoints[player] });
    }
    sortedPlayerPoints.sort(function(a, b) {
        return b.points - a.points;
    });

    for (var i = 0; i < sortedPlayerPoints.length; i++) {
        var listItem = document.createElement("li");
        listItem.textContent = sortedPlayerPoints[i].player + ": " + sortedPlayerPoints[i].points + " points";
        leaderboardList.appendChild(listItem);
    }
}




function buttonClick(i, j) {
    if (id == 0) {
        p1turns++;
        if (p1turns % 2 != 0) {
            p1i = i;
            p1j = j;
        }
    }
    else if (id == 1) {
        p2turns++;
        if (p2turns % 2 != 0) {
            p2i = i;
            p2j = j;
        }
    }
    else if (id == 2) {
        p3turns++;
        if (p3turns % 2 != 0) {
            p3i = i;
            p3j = j;
        }
    }
    else if (id == 3) {
        p4turns++;
        if (p4turns % 2 != 0) {
            p4i = i;
            p4j = j;
        }
    }
    if (p1turns != 0 && p1turns % 2 == 0 && id == 0) {
        U = new UserEvent();
        U.PlayerIdx = "PLAYERONE";
        U.GameId = gameid;

        U.i = p1j;
        U.j = p1i;
        U.k = j;
        U.l = i;
        U.playing = true;
        connection.send(JSON.stringify(U));
        console.log(JSON.stringify(U))
    } else if (p2turns != 0 && p2turns % 2 == 0 && id == 1) {
        U = new UserEvent();
        U.PlayerIdx = "PLAYERTWO";
        U.GameId = gameid;

        U.i = p2j;
        U.j = p2i;
        U.k = j;
        U.l = i;
        U.playing = true;
        connection.send(JSON.stringify(U));
        console.log(JSON.stringify(U))
    } else if (p3turns != 0 && p3turns % 2 == 0 && id == 2) {
        U = new UserEvent();
        U.PlayerIdx = "PLAYERTHREE";
        U.GameId = gameid;

        U.i = p3j;
        U.j = p3i;
        U.k = j;
        U.l = i;
        U.playing = true;
        connection.send(JSON.stringify(U));
        console.log(JSON.stringify(U))
    } else if (p4turns != 0 && p4turns % 2 == 0 && id == 3) {
        U = new UserEvent();
        U.PlayerIdx = "PLAYERFOUR";
        U.GameId = gameid;

        U.i = p4j;
        U.j = p4i;
        U.k = j;
        U.l = i;
        U.playing = true;
        connection.send(JSON.stringify(U));
        console.log(JSON.stringify(U))
    }

    if (wordIsSelectedAndHighlighted) {
        // increment player's points by 1
        playerPoints[playerName]++;
        // updateee the leaderboard
        updateLeaderboard();
    }





}

function fillGrid(array) {
    for (var i = 0; i < 20; i++) {
        for (var j = 0; j < 20; j++) {
            if (array[i][j] == 0) {
                array[i][j] = (char)('A' + RANDOM.nextInt(26)); // Fill empty cells with random letters
            }
            var button = document.getElementById(i + "," + j);
            button.innerHTML = array[i][j];
        }
    }
}
function createTable() {
    var table = document.getElementById("grid");
    for (var i = 0; i < 20; i++) {
        var row = document.createElement('tr');
        for (var j = 0; j < 20; j++) {
            var button = document.createElement("button");
            var data = document.createElement('td');
            button.id = (i + "," + j);
            button.onclick = function (row, col) {
                return function () {
                    buttonClick(row, col);
                };
            }(i, j);

            data.appendChild(button);
            row.appendChild(data);
        } table.appendChild(row);
    }
    document.getElementById("grid").style.display = "none";
    document.getElementById("startButton").style.display = "none";
}
function showRules() {
    var rulesDiv = document.getElementById("rules");
    rulesDiv.style.display = "block";
}
function hideRules() {
    var rulesDiv = document.getElementById("rules");
    rulesDiv.style.display = "none";
}
function updateLobbyStatus(players) {
    var playersDiv = document.getElementById("players");
    playersDiv.innerHTML = "";

    for (var i = 0; i < players.length; i++) {
        var playerDiv = document.createElement("div");
        playerDiv.textContent = players[i];
        playersDiv.appendChild(playerDiv);
    }

    if (players.length >= 2) {
        var startButton = document.getElementById("startButton");
        startButton.style.display = "block";
        startButton.addEventListener("click", function(){
            U = new UserEvent();
            U.start = true
            connection.send(JSON.stringify(U));
        });
    } 
}

function joinLobby() {
    var name = document.getElementById("name").value;
    if (name.trim() !== "") {
        // var msg = {
        //      "Action": "JOIN_LOBBY",
        //      "PlayerName": name
        // };
        lobby = new Lobby;
        lobby.name = name;
        if (id == 0)
        lobby.playeridx = "PLAYERONE";
        else if (id == 1)
        lobby.playeridx = "PLAYERTWO";
        else if (id == 2)
        lobby.playeridx = "PLAYERTHREE";
        else if (id == 3)
        lobby.playeridx = "PLAYERFOUR";
        lobby.status = true; 
        // connection.send(JSON.stringify(msg));
        connection.send(JSON.stringify(lobby));
        inLobby = true;
    }
}
function startTimer() {
    timerInterval = setInterval(updateTimer, 1000);
}
function updateTimer() {
    var minutes = Math.floor(timerSeconds / 60);
    var seconds = timerSeconds % 60;
    timerDisplay.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    if (timerSeconds <= 0) {
        clearInterval(timerInterval);
        timerDisplay.textContent = 'TIMES UP!!'; //whatever you want to display
    } else {
        timerSeconds--;
    }
}

function startGame() {
    var msg = {
        "Action": "START_GAME"
    };
    connection.send(JSON.stringify(msg));

    // Hide the start button
    document.getElementById("startButton").style.display = "none";

    // Hide the lobby and show the game
    document.getElementById("lobby").style.display = "none";
    document.getElementById("game").style.display = "block";
    document.getElementById("grid").style.display = "block";
    document.getElementById("word-bank").style.display = "block";
    document.getElementById("leaderboard").style.display = "block";

    // Check if the timer is already running
    if (!timerInterval) {
        startTimer(); // Start the timer only if it's not already running
    }

    // Populate the leaderboard with player names
    var leaderboardList = document.getElementById("leaderboardList");
    leaderboardList.innerHTML = ""; // Clear existing leaderboard entries

    // Get player names from the lobby and add them to the leaderboard
    var playersDiv = document.getElementById("players");
    var playerNames = playersDiv.getElementsByTagName("div");
    for (var i = 0; i < playerNames.length; i++) {
        var playerName = playerNames[i].textContent;
        var listItem = document.createElement("li");
        listItem.textContent = playerName;
        leaderboardList.appendChild(listItem);
    }

    // Clear the top message
    document.getElementById("topMessage").innerHTML = "";

    // Display the players' names side by side
    var gamePlayersDiv = document.getElementById("gamePlayers");
    gamePlayersDiv.innerHTML = "";
    for (var i = 0; i < playerNames.length; i++) {
        gamePlayersDiv.innerHTML += "<div>" + playerNames[i].textContent + "</div>";
    }
}


