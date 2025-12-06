package snake

import scala.io.Source
import scalafx.scene.{Group, PerspectiveCamera, Scene}
import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Represents the game level, initialized from a file.
 *
 * @param file Path to the level file
 */
class Level(file: String) extends Group {

  // Read the level file to initialize the level
  val bufferedSource = Source.fromFile("src/main/scala/" + file)
  var y = 0 // Initialize y coordinate
  var z = -1 // Initialize z coordinate
  var lvl: ListBuffer[ListBuffer[ListBuffer[Block]]] = ListBuffer() // Create a 3D ListBuffer to store blocks
  var snake: Snake = null // Initialize snake object
  var food: Food = null // Initialize food object

  // Loop through each line in the level file
  for (line <- bufferedSource.getLines) {
    if (!line.forall(Character.isDigit)) {
      // Ensure the ListBuffer has enough dimensions for z
      while (lvl.length <= z) {
        lvl += ListBuffer()
      }

      // Ensure the ListBuffer(z) has enough dimensions for y
      while (lvl(z).length <= y) {
        lvl(z) += ListBuffer()
      }

      // Loop through each character in the line
      for ((char, x) <- line.zipWithIndex) {
        char match {
          case 'W' =>
            // Create a wall and corresponding block at the specified coordinates
            val wall = new Wall(x, y, z)
            children.add(wall)
            val block = new Block(x, y, z)
            block.occupiedBy = "WALL"
            children.add(block)
            lvl(z)(y) += block
          case 'S' =>
            // Create the snake and corresponding block at the specified coordinates
            snake = new Snake(x, y, z, this)
            children.add(snake)
            val block = new Block(x, y, z)
            children.add(block)
            lvl(z)(y) += block
          case _ =>
            // Create an empty block at the specified coordinates for other characters
            val block = new Block(x, y, z)
            children.add(block)
            lvl(z)(y) += block
        }
      }
      y += 1 // Move to the next y coordinate
    } else if (line.forall(Character.isDigit)) {
      z += 1 // Move to the next z coordinate
      y = 0 // Reset y to 0 for the next row
    }
  }
  bufferedSource.close // Close the file

  // Initialize food at a random empty block location
  randomFoodLocation()

  // Function to generate food at a random empty block location
  def randomFoodLocation(): Unit = {
    var block = getRandomEmptyBlock()
    food = new Food(block.x, block.y, block.z)
    block.occupiedBy = "FOOD"
    children.add(food)
  }

  // Function to get a random empty block
  def getRandomEmptyBlock(): Block = {
    var block = lvl(Random.nextInt(lvl.length))(Random.nextInt(lvl(0).length))(Random.nextInt(lvl(0)(0).length))
    while (!block.occupiedBy.equals("EMPTY") || (snake != null && checkCollision(block.x, block.y, block.z))) {
      block = lvl(Random.nextInt(lvl.length))(Random.nextInt(lvl(0).length))(Random.nextInt(lvl(0)(0).length))
    }
    block
  }

  // Function to check for collisions with the snake
  def checkCollision(x: Int, y: Int, z: Int): Boolean = {
    var current: Snake = snake
    while (current != null) {
      if (current.translateX.toInt == x && current.translateY.toInt == y && current.translateZ.toInt == z) {
        return true
      }
      current = current.next
    }
    false
  }
}
