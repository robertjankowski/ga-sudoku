package ga

import generator.Sudoku.Grid

object Fitness {

  /**
    * Fitness function works as follow:
    *
    * E.g for rows (the same for columns and 3x3 blocks):
    * f = sum over rows  { 9 - no. of distinct value in row } // how many incorrect values are in the single row
    *
    */
  def calculateFitness(grid: Grid): Int =
    checkRows(grid) + checkColumns(grid) + checkSquares(grid)

  private def checkRows(grid: Grid) =
    grid.map {
      g => g.length - g.distinct.length
    }.sum

  private def checkColumns(grid: Grid) = checkRows(grid.transpose)

  private def checkSquares(grid: Grid) = {
    var boxes = Array.empty[Array[Int]]
    for {
      r <- 0 until 3
      c <- 0 until 3
    } {
      var b = Array.empty[Int]
      for {
        i <- 0 until 3
        j <- 0 until 3
      } b +:= grid(3 * r + i)(3 * c + j)
      boxes +:= b
    }
    checkRows(boxes)
  }

}
