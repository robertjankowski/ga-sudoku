package solver

import generator.Sudoku

import scala.concurrent.Future

trait Solver {

  def solve(sudoku: Sudoku): Future[Sudoku]
}
