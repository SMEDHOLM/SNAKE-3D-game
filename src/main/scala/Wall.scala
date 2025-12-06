package snake

import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.DrawMode

/**
 * Represents a Wall entity in the game level.
 *
 * @param x Initial x-coordinate of the wall
 * @param y Initial y-coordinate of the wall
 * @param z Initial z-coordinate of the wall
 */
class Wall(x: Int, y: Int, z: Int) extends Block(x: Int, y: Int, z: Int) {
  drawMode = DrawMode.Fill
  material = new PhongMaterial(Color.White)
  occupiedBy = "WALL"
}
