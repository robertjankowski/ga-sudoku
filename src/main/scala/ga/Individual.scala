package ga

import generator.Sudoku.Grid
import generator.Sudoku.GRID_SIZE

import scala.util.Random

class Individual(val grid: Grid)(implicit random: Random) {

  /**
    * Own grid is permutation of 1 to 9 ranges
    */
  private var ownGrid: Grid = createGrid
  var fitness: Double = 0

  def updateFitness(): Unit = {
    fitness = Fitness.calculateFitness(ownGrid, grid)
  }

  private def createGrid =
    Array.range(1, 9).map(_ => (1 to GRID_SIZE).toArray)

}
