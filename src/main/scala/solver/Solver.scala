package solver

import generator.Sudoku.Grid

import scala.concurrent.Future

trait Solver {

  def solve(sudoku: Grid): Future[Grid]
}
