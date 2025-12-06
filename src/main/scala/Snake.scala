package snake

import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.DrawMode
import com.sun.javafx.scene.traversal.Direction
import snake.Snake3dApp.restartScene

/**
 * Represents the Snake entity in the game.
 *
 * @param nx     Initial x-coordinate of the Snake's head
 * @param ny     Initial y-coordinate of the Snake's head
 * @param nz     Initial z-coordinate of the Snake's head
 * @param level  Reference to the game level
 */
class Snake(var nx: Int, var ny: Int, var nz: Int, var level: Level) extends Block(nx: Int, ny: Int, nz: Int) {
  drawMode = DrawMode.Fill
  material = new PhongMaterial(Color.GreenYellow)
  occupiedBy = "Snake"
  var next: Snake = null
  var direction = Direction.RIGHT
  val MoveDistance = 100

  /**
   * Moves the snake based on its current direction.
   */
  def move(): Unit = {
    direction match {
      case Direction.RIGHT => moveTo(translateX.toInt + MoveDistance, translateY.toInt, translateZ.toInt)
      case Direction.LEFT => moveTo(translateX.toInt - MoveDistance, translateY.toInt, translateZ.toInt)
      case Direction.UP => moveTo(translateX.toInt, translateY.toInt - MoveDistance, translateZ.toInt)
      case Direction.DOWN => moveTo(translateX.toInt, translateY.toInt + MoveDistance, translateZ.toInt)
      case Direction.NEXT => moveTo(translateX.toInt, translateY.toInt, translateZ.toInt + MoveDistance)
      case Direction.PREVIOUS => moveTo(translateX.toInt, translateY.toInt, translateZ.toInt - MoveDistance)
    }
  }

  /**
   * Moves the snake to the specified coordinates if possible.
   *
   * @param x Target x-coordinate
   * @param y Target y-coordinate
   * @param z Target z-coordinate
   */
  def moveTo(x: Int, y: Int, z: Int): Unit = {
    try {
      val block = level.lvl(z / 100)(y / 100)(x / 100)
      if (block.occupiedBy.equals("EMPTY")) {
        if (next != null && checkCollision(x, y, z, exclude = this)) {
          // Collision with snake body, handle it by removing tail segments
          removeTailUntilCollision(x, y, z)
        } else {
          if (next != null) {
            next.moveTo(translateX.toInt, translateY.toInt, translateZ.toInt)
          }
          translateX = x.toDouble
          translateY = y.toDouble
          translateZ = z.toDouble
        }
      } else if (block.occupiedBy.equals("FOOD")) {
        block.occupiedBy = "EMPTY"
        level.children.removeAll(level.food)
        grow()
        level.randomFoodLocation()
        moveTo(x, y, z)
        Snake3dApp.snakeAteFood()
      } else {
        // Collision with non-empty block (e.g., walls), restart the scene
        restartScene()
      }
    } catch {
      case _: Throwable => // Handle exceptions, for simplicity, restart the scene
        restartScene()
    }
  }

  /**
   * Checks if there is a collision at the specified coordinates.
   *
   * @param x       X-coordinate to check
   * @param y       Y-coordinate to check
   * @param z       Z-coordinate to check
   * @param exclude Snake segment to exclude from collision check
   * @return True if collision detected, false otherwise
   */
  def checkCollision(x: Int, y: Int, z: Int, exclude: Snake = null): Boolean = {
    var current: Snake = this
    while (current != null) {
      if (current != exclude && current.translateX.toInt == x && current.translateY.toInt == y && current.translateZ.toInt == z) {
        return true // Collision detected
      }
      current = current.next
    }
    false // No collision
  }

  /**
   * Removes tail segments until a collision occurs at specified coordinates.
   *
   * @param x X-coordinate of collision
   * @param y Y-coordinate of collision
   * @param z Z-coordinate of collision
   */
  def removeTailUntilCollision(x: Int, y: Int, z: Int): Unit = {
    var current: Snake = this
    var previous: Snake = null

    // Find the colliding segment
    while (current != null && (current.translateX.toInt != x || current.translateY.toInt != y || current.translateZ.toInt != z)) {
      previous = current
      current = current.next
    }

    // Remove tail segments until the colliding segment
    if (current != null) {
      while (current != null) {
        level.children.remove(current)
        current = current.next
      }
      if (previous != null) {
        // Update the 'next' reference of the previous segment
        previous.next = null
      } else {
        // The head itself collided, set 'next' to null
        next = null
      }
    }
  }

  /**
   * Increases the length of the snake by adding a new segment.
   */
  def grow(): Unit = {
    if (next != null) {
      next.grow()
    } else {
      next = new Snake(this.translateX.toInt, this.translateY.toInt, this.translateZ.toInt, level)
      // Update the position based on the current direction
      direction match {
        case Direction.RIGHT => next.translateX.value -= MoveDistance
        case Direction.LEFT => next.translateX.value += MoveDistance
        case Direction.UP => next.translateY.value += MoveDistance
        case Direction.DOWN => next.translateY.value -= MoveDistance
        case Direction.NEXT => next.translateZ.value -= MoveDistance
        case Direction.PREVIOUS => next.translateZ.value += MoveDistance
      }
      level.children.add(next)
    }
  }
}
