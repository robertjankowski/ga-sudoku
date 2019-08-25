package ga

import generator.SudokuGenerator
import generator.Sudoku.Grid
import generator.Sudoku.GRID_SIZE

class Individual(val grid: Grid) {

  /**
    * Own grid is permutation of 1 to 9 ranges
    */
  var ownGrid: Grid = createGrid
  var fitness: Double = 0

  def updateFitness(): Unit =
    fitness = Fitness.calculateFitness(ownGrid, grid)

  def transposeGrid(): Unit = {
    ownGrid = ownGrid.transpose
  }

  private def createGrid = {
    val g = Array.range(1, 10).map(_ => (1 to GRID_SIZE).toArray)
    new SudokuGenerator(g).generate().g
  }
}
