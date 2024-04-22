<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Word Search Game</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-image: url('game.jpg'); 
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0;
            padding: 0;
        }
        
        #container {
            max-width: 600px; /* Increased maximum width */
            width: 500%;
            margin: 50px auto;
            background-color: rgba(0, 0, 0, 0.7); /* Semi-transparent black background */
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            color: #ffffff; /* Text color */
            position: relative;
        }

        #header {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        #lobby {
            margin-bottom: 20px;
        }

        #lobby label {
            display: block;
            margin-bottom: 10px;
            color: #ffffff; /* Text color */
        }

        #lobby input[type="text"] {
            padding: 10px;
            margin: 10px 0;
            width: calc(100% - 20px); /* Adjusted width */
            box-sizing: border-box;
            border-radius: 5px;
            border: 1px solid #ffffff; /* Border color */
            background-color: rgba(255, 255, 255, 0.1); /* Semi-transparent white background */
            color: #ffffff; /* Text color */
        }

        #lobby button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #ffffff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        #lobby button:hover {
            background-color: #0056b3;
        }

        #game {
            display: none;
        }

        #players {
            font-weight: bold;
            margin-bottom: 10px;
        }

        #bottomMessage {
            margin-top: 20px;
            font-style: italic;
        }

        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #ffffff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        input[type="text"] {
            padding: 10px;
            margin: 10px 0;
            width: 100%;
            box-sizing: border-box;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        #rules {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            background-color: rgba(0, 0, 0, 0.7); /* Semi-transparent black background */
            padding: 20px;
            border-radius: 10px;
            color: #ffffff; /* Text color */
        }

        #closeButton {
            position: absolute;
            top: 10px;
            right: 10px;
            background-color: transparent;
            border: none;
            color: #ffffff; /* Text color */
            font-size: 16px;
            cursor: pointer;
        }

    </style>
</head>
<body>

    <div id="container">
        <div id="header">Word Search Game</div>

        <div id="lobby">
            <label for="name">Enter your name:</label>
            <input type="text" id="name" placeholder="Enter name">
            <button onclick="joinLobby()">Join Lobby</button>
            <p id="lobbyStatus">Players in the lobby:</p>
            <div id="players"></div>
        </div>
        <div id="game">
            <p id="gameStatus">Game is starting...</p>
            <div id="gamePlayers"></div>
        </div>
        <table id="grid"></table>

        <button id="startButton" onclick="startGame()" style="display:none;">Start Game</button>
        <button id="rulesButton" onclick="showRules()">Rules</button>

        <div id="rules">
            <button id="closeButton" onclick="hideRules()">X</button>
            <p>Rules:</p>
            <p>1. Upon entering the game, choose a unique nickname to identify yourself. 
            <p>2. Once there's more than one player, players can start the game by clicking on start game.
            <p>3. Words can be horizontal, vertical, diagonal, or reversed.
            <p>4. Players can select words by clicking on the starting and ending letters with their mouse.
            <p>5. Guess the hidden words within a limited timeframe. 
            <p>6. A list of words used in the grid is provided for players to find
            <p>7. Points are awarded based on the length and of the word.
            <p>                           Good Luck!
            </p>
        </div>

        <label id="topMessage"></label>
        <label id="bottomMessage"></label>
    </div>


</body>

</html>

 <script>
     var connection = null;
     var game = null;
     var playerName = "";
     var inLobby = false;
     var id =0;
     var gameid = -1;
     class Lobby {
        Name = null;
        }

     var serverUrl = "ws://" + window.location.hostname + ":9880";
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
 
         if ('Players' in obj) {
            updateLobbyStatus(obj.Players);
            var playersDiv = document.getElementById("players");
            playersDiv.innerHTML = "";
 
            for (var i = 0; i < obj.Players.length; i++) {
                var playerDiv = document.createElement("div");
                playerDiv.textContent = obj.Players[i];
                playersDiv.appendChild(playerDiv);

                if(obj.Players.length >= 2){
                    document.getElementById("startButton").style.display = "block";
                } else {
                    document.getElementById("startButton").style.display = "none";
                }
            }
         }
         if ('cells' in obj){
            fillGrid(obj.cells);
         }
     }
     
   
     function fillGrid(array) {
        for(var i =0; i< 50; i++){
            for(var j=0; j<50; j++){
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
        for(var i =0; i<50; i++){
            var row = document.createElement('tr');

            for(var j=0; j<50; j++){
                var button = document.createElement("button");
                var data = document.createElement('td');
                button.id = (i + "," + j);
                button.class = "myButton";

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
        //  var playersDiv = document.getElementById("players");
        //  playersDiv.innerHTML = "";
 
        //  for (var i = 0; i < players.length; i++) {
        //      var playerDiv = document.createElement("div");
        //      playerDiv.textContent = players[i];
        //      playersDiv.appendChild(playerDiv);
        //  }
 
        //  if (players.length >= 2) {
        //      document.getElementById("startButton").style.display = "block";
        //  } else {
        //      document.getElementById("startButton").style.display = "none";
        //  }
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
 
     function startGame() {
         var msg = {
             "Action": "START_GAME"
         };
         connection.send(JSON.stringify(msg));
 
         // Hide the lobby and show the game
         document.getElementById("lobby").style.display = "none";
         document.getElementById("game").style.display = "block";
         document.getElementById("grid").style.display = "block";
 
         // Display the players' names side by side
         var playersDiv = document.getElementById("players");
         var playerNames = playersDiv.getElementsByTagName("div");
         var gamePlayersDiv = document.getElementById("gamePlayers");
         gamePlayersDiv.innerHTML = "";
         for (var i = 0; i < playerNames.length; i++) {
             gamePlayersDiv.innerHTML += "<div>" + playerNames[i].textContent + "</div>";
         }
     }
 </script>

</body>
</html>
