

const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

// Define player properties
let player = {
  x: 50,
  y: 50,
  width: 50,
  height: 50,
  speed: 5,
  jumping: false,
};

// Draw the player on the canvas
function drawPlayer() {
  ctx.fillStyle = "blue";
  ctx.fillRect(player.x, player.y, player.width, player.height);
}

// Move the player
function movePlayer() {
  if (rightPressed && player.x < canvas.width - player.width) {
    player.x += player.speed;
  } else if (leftPressed && player.x > 0) {
    player.x -= player.speed;
  }

  if (jumpPressed && !player.jumping) {
    player.jumping = true;
    player.yVelocity = -player.speed * 2;
  }

  if (player.jumping) {
    player.yVelocity += 0.2;
    player.y += player.yVelocity;

    if (player.y > canvas.height - player.height) {
      player.jumping = false;
      player.y = canvas.height - player.height;
      player.yVelocity = 0;
    }
  }
}

// Game loop
function gameLoop() {
  // Clear the canvas
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Draw the player
  drawPlayer();

  // Move the player
  movePlayer();

  // Call gameLoop again
  requestAnimationFrame(gameLoop);
}

// Start the game loop
requestAnimationFrame(gameLoop);


let rightPressed = false;
let leftPressed = false;
let jumpPressed = false;

document.addEventListener("keydown", (event) => {
  if (event.code === "ArrowRight") {
    rightPressed = true;
  } else if (event.code === "ArrowLeft") {
    leftPressed = true;
  } else if (event.code === "Space") {
    jumpPressed = true;
  }
});

document.addEventListener("keyup", (event) => {
  if (event.code === "ArrowRight") {
    rightPressed = false;
  } else if (event.code === "ArrowLeft") {
    leftPressed = false;
  } else if (event.code === "Space") {
    jumpPressed = false;
  }
});


let platforms = [
  {
    x: 0,
    y: 550,
    width: 800,
    height: 50,
  },
  {
    x: 300,
    y: 400,
    width: 200,
    height: 25,
  },
];

// Draw platforms on the canvas
function drawPlatforms() {
  ctx.fillStyle = "green";
  platforms.forEach((platform) => {
    ctx.fillRect(platform.x, platform.y, platform.width, platform.height);
  });
}


function checkCollision(player, platform) {
  let bottom = player.y + player.height >= platform.y;
  let top = player.y <= platform.y + platform.height;
  let right = player.x + player.width >= platform.x;
  let left = player.x <= platform.x + platform.width;

  return bottom && top && right && left;
}

function handleCollision() {
  platforms.forEach((platform) => {
    if (checkCollision(player, platform)) {
      if (player.yVelocity > 0) {
        player.jumping = false;
        player.y = platform.y - player.height;
        player.yVelocity = 0;
      } else if (player.yVelocity < 0) {
        player.y = platform.y + platform.height;
        player.yVelocity = 0;
      }
    }
  });
}


let enemies = [
  {
    x: 700,
    y: 500,
    width: 50,
    height: 50,
    speed: 3,
    direction: "left",
  },
];

// Draw enemies on the canvas
function drawEnemies() {
  ctx.fillStyle = "red";
  enemies.forEach((enemy) => {
    ctx.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
  });
}

function checkEnemyCollision(player, enemy) {
  let bottom = player.y + player.height >= enemy.y;
  let top = player.y <= enemy.y + enemy.height;
  let right = player.x + player.width >= enemy.x;
  let left = player.x <= enemy.x + enemy.width;

  return bottom && top && right && left;
}

function handleEnemyCollision() {
  enemies.forEach((enemy) => {
    if (checkEnemyCollision(player, enemy)) {
      // Game over
      console.log("Game over");
    }
  });
}

let score = 0;
let scoreThreshold = 700;

function handleScore() {
  if (player.x > scoreThreshold) {
    score += 10;
    scoreThreshold += 700;
  }

  ctx.font = "20px Arial";
  ctx.fillStyle = "black";
  ctx.fillText(`Score: ${score}`, 10, 30);
}


function gameOver() {
  cancelAnimationFrame(animationId);
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.font = "50px Arial";
  ctx.fillStyle = "red";
  ctx.textAlign = "center";
  ctx.fillText("Game Over", canvas.width / 2, canvas.height / 2);
}

// Connection stuff
var conn;
var log = document.getElementById("log");
var msg = document.getElementById("msg");

function connect(serverURI) {
    conn = new WebSocket(serverURI);
    conn.onmessage = function(msgevent) {
       var msg = JSON.parse(msgevent.data);
       receivedMessage(msg);
    };
}

function receivedMessage(msg){
       console.log('Received:', msg);
}

function sendToserver(message) {
    const connectMessage = {
        command: "LOGIN",
        username: 'jehamming',
        password: hex_md5('jehamming')
     };

    sendMessage(conn, JSON.stringify(connectMessage));
}

const waitForOpenConnection = (socket) => {
    return new Promise((resolve, reject) => {
        const maxNumberOfAttempts = 10
        const intervalTime = 200 //ms

        let currentAttempt = 0
        const interval = setInterval(() => {
            if (currentAttempt > maxNumberOfAttempts - 1) {
                clearInterval(interval)
                reject(new Error('Maximum number of attempts exceeded'))
            } else if (socket.readyState === socket.OPEN) {
                clearInterval(interval)
                resolve()
            }
            currentAttempt++
        }, intervalTime)
    })
}

const sendMessage = async (socket, msg) => {
    if (socket.readyState !== socket.OPEN) {
        try {
            await waitForOpenConnection(socket)
            socket.send(msg)
        } catch (err) { console.error(err) }
    } else {
        socket.send(msg)
    }
}

//

function gameLoop() {
  // Clear the canvas
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Draw the player
  drawPlayer();

  // Move the player
  movePlayer();

  // Draw platforms on the canvas
  drawPlatforms();

  // Handle collisions between the player and platforms
  handleCollision();

  // Draw enemies on the canvas
  drawEnemies();

  // Handle collisions between the player and enemies
  handleEnemyCollision();

  // Handle the score
  handleScore();

  // Check if the player is off-screen
  if (player.y > canvas.height) {
    gameOver();
    return;
  }

  // Call gameLoop again
  animationId = requestAnimationFrame(gameLoop);
}

// Connect
connect("ws://127.0.0.1:8887/");
sendToserver("whatever");

// Start the game loop
//let animationId = requestAnimationFrame(gameLoop);

ctx.clearRect(0, 0, canvas.width, canvas.height);
ctx.font = "50px Arial";
ctx.fillStyle = "black";
ctx.textAlign = "center";
ctx.fillText("Press Space to Start", canvas.width / 2, canvas.height / 2);

document.addEventListener("keydown", (event) => {
  if (event.code === "Space") {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    animationId = requestAnimationFrame(gameLoop);
  }
});