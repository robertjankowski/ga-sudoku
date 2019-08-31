package ga

import generator.Sudoku.Grid

import scala.util.Random

class Individual(val grid: Grid) {

  /**
   * Own grid is a permutation of 1 to 9 ranges
   */
  var ownGrid: Grid = createGrid
  var fitness: Double = 0

  def updateFitness(): Unit =
    fitness = Fitness.calculateFitness(ownGrid, grid)

  def transposeGrid(): Unit = {
    ownGrid = ownGrid.transpose
  }

  private def createGrid = {
    //    val g = Array.range(1, 10).map(_ => (1 to GRID_SIZE).toArray)
    //    new SudokuGenerator(g).generate().g
    val r = new Random()
    Array.fill(9, 9)(1 + r.nextInt(9))
  }

}
