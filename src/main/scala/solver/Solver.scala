package solver

import generator.Sudoku.Grid

trait Solver {

  def solve(sudoku: Grid): Grid
}
