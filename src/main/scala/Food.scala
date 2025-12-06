package snake

import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.DrawMode

/**
 * Represents a Food entity in the game level.
 *
 * @param x Initial x-coordinate of the food item
 * @param y Initial y-coordinate of the food item
 * @param z Initial z-coordinate of the food item
 */
class Food(x: Int, y: Int, z: Int) extends Block(x: Int, y: Int, z: Int) {
  drawMode = DrawMode.Fill
  material = new PhongMaterial(Color.Red)
  occupiedBy = "FOOD"
}
