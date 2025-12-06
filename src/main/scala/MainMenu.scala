package snake

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{HBox, VBox}

object MainMenu extends JFXApp3 {

  override def start(): Unit = {
    stage = new PrimaryStage {
      title = "Main Menu"
      scene = createMainMenuScene(1280, 800) // Set initial scene width and height here
    }
  }

  def createMainMenuScene(sceneWidth: Double, sceneHeight: Double): Scene = {
    val startButton = new Button("Start")
    val exitButton = new Button("Exit")

    // Set button sizes
    val buttonWidth = 200
    val buttonHeight = 60
    startButton.prefWidth = buttonWidth
    startButton.prefHeight = buttonHeight
    exitButton.prefWidth = buttonWidth
    exitButton.prefHeight = buttonHeight

    // Set button actions
    startButton.onAction = _ => Snake3dApp.start()
    exitButton.onAction = _ => stage.close()

    // Create an HBox to center the buttons horizontally
    val buttonBox = new HBox(20) {
      alignment = Pos.Center
      children = Seq(startButton, exitButton)
    }

    // Create a VBox to center the HBox vertically
    val layout = new VBox {
      alignment = Pos.Center
      padding = Insets(50)
      children = Seq(buttonBox)
    }

    new Scene(layout, sceneWidth, sceneHeight) // Create scene with specified width and height
  }
}
