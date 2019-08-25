package generator

import generator.Sudoku.Grid

import scala.util.Random

class SudokuGenerator(grid: Grid) {

  private val r = new Random()

  def generate(): Sudoku = {
    val shuffledRows = shuffleRows(grid)
    val shuffledCols = shuffleCols(shuffledRows)
    new Sudoku(shuffledCols)
  }

  private def shuffleRows(grid: Grid): Grid = {
    val shuffled: Array[Grid] = grid.sliding(3, 3).toArray
    shuffled.flatMap { grid =>
      val copyGrid = grid.clone()
      val shuffledIndices = r.shuffle(grid.indices.toList)
      for (i <- grid.indices) {
        copyGrid(i) = grid(shuffledIndices(i))
      }
      copyGrid
    }
  }

  private def shuffleCols(grid: Grid): Grid = {
    shuffleRows(grid.transpose).transpose
  }

}
