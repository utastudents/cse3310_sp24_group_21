     var connection = null;
     var game = null;
     var playerName = "";
     var inLobby = false;
     var id =0;
     var gameid = -1;
     var p1turns = 0;
     var p2turns = 0;
     var p1i,p1j,p2i,p2j,c,d = 0;
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
            }else if (obj.YouAre == "PLAYERTWO"){
                id = 1;
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
                    }bank.appendChild(word);
                }
            }count++;
         }

         if ('cells' in obj){
            fillGrid(obj.cells);
            if (gameid == obj.GameId){
                for(var i =0; i< 20; i++){
                    for(var j=0; j<20; j++){
                        //show colors to players
                        var v = document.getElementById(i + "," + j);
                        if(obj.Button[i][j] == "PLAYERONE")
                        v.style.backgroundColor = "red";
                        else if(obj.Button[i][j] == "PLAYERTWO")
                        v.style.backgroundColor = "blue";
                    }
                }
            }
         }
     }
     
     function buttonClick(i, j) {
        if (id == 0) {
            p1turns++;
            if (p1turns % 2 != 0){
                p1i = i;
                p1j = j;
            }
        }
        else if (id == 1) {
            p2turns++;
            if (p2turns % 2 != 0){
                p2i = i;
                p2j = j;
            }
        }
        if (p1turns != 0 && p1turns % 2 == 0 && id == 0) {
            U = new UserEvent();
            if (id == 0)
            U.PlayerIdx = "PLAYERONE";
            else if (id == 1)
            U.PlayerIdx = "PLAYERTWO";
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
            if (id == 0)
            U.PlayerIdx = "PLAYERONE";
            else if (id == 1)
            U.PlayerIdx = "PLAYERTWO";
            U.GameId = gameid;
          
            U.i = p2j;
            U.j = p2i;
            U.k = j;
            U.l = i; 
            U.playing = true;
            connection.send(JSON.stringify(U));
            console.log(JSON.stringify(U))
        } 
    }
     
     function fillGrid(array) {
        for(var i =0; i< 20; i++){
            for(var j=0; j<20; j++){
                if (array[i][j] == 0) {
                    array[i][j] = (char) ('A' + RANDOM.nextInt(26)); // Fill empty cells with random letters
                }
                var button = document.getElementById(i + "," + j);
                button.innerHTML = array[i][j];
            }
        }
     }
     function createTable() {
        var table = document.getElementById("grid");
        for(var i =0; i<20; i++){
            var row = document.createElement('tr');
            for(var j=0; j<20; j++){
                var button = document.createElement("button");
                var data = document.createElement('td');
                button.id = (i + "," + j);
                button.onclick = function(row, col) {
                    return function() {
                        buttonClick(row, col);
                    };
                }(i, j);
                
                data.appendChild(button);
                row.appendChild(data);
            }table.appendChild(row);
        }
        document.getElementById("grid").style.display = "none";
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
              document.getElementById("startButton").style.display = "block";
          } else {
              document.getElementById("startButton").style.display = "none";
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

    // The rest of your startGame function
    // Hide the lobby and show the game
    document.getElementById("lobby").style.display = "none";
    document.getElementById("game").style.display = "block";
    document.getElementById("grid").style.display = "block";
    document.getElementById("word-bank").style.display = "block";
    startTimer(); 
    // Clear the top message
    document.getElementById("topMessage").innerHTML = "";
    // Display the players' names side by side
    var playersDiv = document.getElementById("players");
    var playerNames = playersDiv.getElementsByTagName("div");
    var gamePlayersDiv = document.getElementById("gamePlayers");
    gamePlayersDiv.innerHTML = "";
    for (var i = 0; i < playerNames.length; i++) {
        gamePlayersDiv.innerHTML += "<div>" + playerNames[i].textContent + "</div>";
    }
}
