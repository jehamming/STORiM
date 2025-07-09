var ctx;
var canvas;
// Define player properties
let player = {
  x: 50,
  y: 50,
  width: 50,
  height: 50,
  speed: 5,
  jumping: false,
};

function initialize() {
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");

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

 }

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

  // Check if the player is off-screen
  if (player.y > canvas.height) {
    gameOver();
    return;
  }

  // Call gameLoop again
  animationId = requestAnimationFrame(gameLoop);
}

