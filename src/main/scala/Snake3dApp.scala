package snake

import com.sun.javafx.scene.traversal.Direction
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.geometry.Point3D
import scalafx.scene.{AmbientLight, Group, PerspectiveCamera, Scene}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.{Box, DrawMode}
import scalafx.scene.transform.{Rotate, Translate}

import scala.util.Random
import scalafx.animation.AnimationTimer
import scalafx.animation.PauseTransition
import scalafx.util.Duration
import scalafx.scene.control.Label
import scalafx.scene.text.Font

// Define the main object for the Snake 3D application
object Snake3dApp extends JFXApp3 {

  // Define constants for box movement
  val MoveDistance = 100
  var canMove = true
  val moveDelay = Duration(1000) // Adjust the delay duration as needed
  var gameStarted = false // Flag to check whether the game has started or not
  var snake: Snake = _
  var root = new Group()
  var eatenFoodCount = 0 // Counter for eaten food items
  var foodCounterLabel: Label = _ // Label for eaten food count

  // Override the start method of JFXApp3
  override def start(): Unit = {
    // Load level from file
    var level = Level("level.txt")
    root = new Group(level)
    snake = level.snake

    // Create a camera with initial translation and rotation
    val camera = new PerspectiveCamera(false)
    camera.getTransforms.addAll(
      new Translate(-200, -200, 600), // Initial translation
      new Rotate(60, Rotate.YAxis),    // Rotate around Y-axis by 45 degrees
      new Rotate(-30, Rotate.XAxis)    // Rotate around X-axis by -30 degrees
    )

    // Create ambient light
    val ambientLight = new AmbientLight(Color.White)

    // Add the camera and ambient light to the root group
    root.children.addAll(camera, ambientLight)

    // Create the 3D scene
    val scenee = new Scene(root, 1280, 720, true)
    scenee.fill = Color.Black

    // Set the camera for the scene
    scenee.camera = camera

    // Add eaten food counter label
    foodCounterLabel = new Label(s"Eaten Food: $eatenFoodCount")
    foodCounterLabel.layoutX = -600 // Position from left
    foodCounterLabel.layoutY = -350 // Position from top
    foodCounterLabel.font = Font("Arial", 24) // Set font size
    foodCounterLabel.textFill = Color.White // Set text color to white
    foodCounterLabel.transforms += new Rotate(60, Rotate.YAxis)
    root.children.add(foodCounterLabel)

    // Handle keyboard input for box movement
    scenee.onKeyPressed = (event: KeyEvent) => handleKeyPress(event)

    // Set up the stage
    stage = new JFXApp3.PrimaryStage {
      title = "Snake 3D Scene"
      scene = scenee

      // Start the animation timer
      AnimationTimer { currentTime =>
        if (gameStarted && canMove) {
          snake.move()

          // Check for collisions or other game logic here
          updateFoodCounterLabel()


          canMove = false
          new PauseTransition(moveDelay) {
            onFinished = _ => canMove = true
          }.play()
        }
      }.start()
    }
  }

  // Update the eaten food counter label
  def updateFoodCounterLabel(): Unit = {
    foodCounterLabel.text = s"Eaten Food: $eatenFoodCount"
  }

  // Increase food counter
  def snakeAteFood(): Unit = {
    eatenFoodCount += 1
  }

  // Restart the game scene
  def restartScene(): Unit = {
    // Stop the current animation timer
    stage.getScene.getWindow.getOnCloseRequest match {
      case handler: javafx.event.EventHandler[_] => handler.handle(null)
      case _ => // No close request handler found
    }

    // Clear the root group
    root.children.clear()
    eatenFoodCount = 0
    gameStarted = false
    start()
  }

  // Handle keyboard input
  def handleKeyPress(event: KeyEvent): Unit = {
    if (!gameStarted) {
      // If the game hasn't started, start it now
      gameStarted = true
    }
    event.code match {
      case KeyCode.W if snake.direction != Direction.DOWN => snake.direction = Direction.UP
      case KeyCode.S if snake.direction != Direction.UP => snake.direction = Direction.DOWN
      case KeyCode.A if snake.direction != Direction.RIGHT => snake.direction = Direction.LEFT
      case KeyCode.D if snake.direction != Direction.LEFT => snake.direction = Direction.RIGHT
      case KeyCode.E if snake.direction != Direction.NEXT => snake.direction = Direction.PREVIOUS
      case KeyCode.Q if snake.direction != Direction.PREVIOUS => snake.direction = Direction.NEXT
      case _ =>
    }
  }
}
