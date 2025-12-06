package snake

import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.{Box, DrawMode}
import scala.util.Random

/**
 * Represents a block in the game level grid.
 *
 * @param nx Initial x-coordinate of the block
 * @param ny Initial y-coordinate of the block
 * @param nz Initial z-coordinate of the block
 */
class Block(nx: Int, ny: Int, nz: Int) extends Box {
  // Initialize block coordinates
  var x = nx
  var y = ny
  var z = nz

  // Set block size
  var blockSize = 100
  width = blockSize
  height = blockSize
  depth = blockSize

  // Set block draw mode and position
  drawMode = DrawMode.Line
  translateX = x * blockSize
  translateY = y * blockSize
  translateZ = z * blockSize

  // Set random color for the block
  material = new PhongMaterial(Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)))

  // Set initial occupancy status
  var occupiedBy = "EMPTY"
}
