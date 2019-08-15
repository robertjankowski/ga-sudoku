package ga

import generator.Sudoku

class Fitness {

  /**
   * Fitness function works as follow:
   *
   * E.g for rows (the same for columns and 3x3 blocks):
   * f = sum over rows  { 9 - no. of distinct value in row } // how many incorrect values are in the single row
   */
  def calculateFitness(sudoku: Sudoku): Int = checkRows(sudoku.g) + checkColumns(sudoku.g) + checkSquares(sudoku.g)

  private def checkRows(grid: Sudoku.Grid) =
    grid.map {
      g => g.length - g.distinct.length
    }.sum

  private def checkColumns(grid: Sudoku.Grid) = checkRows(grid.transpose)

  private def checkSquares(grid: Sudoku.Grid) = {
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
