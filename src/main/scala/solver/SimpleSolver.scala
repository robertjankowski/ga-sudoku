package solver

import generator.Sudoku
import generator.Sudoku.{GRID_SIZE, Grid}

import scala.concurrent.{ExecutionContext, Future}

class SimpleSolver(implicit executionContext: ExecutionContext) extends Solver {
  private val s = Math.sqrt(GRID_SIZE).toInt

  override def solve(sudoku: Sudoku): Future[Sudoku] = {
    def solving(grid: Grid, cell: Int = 0): Option[Grid] = (cell % GRID_SIZE, cell / GRID_SIZE) match {
      case (r, `GRID_SIZE`) => Some(grid)
      case (r, c) if grid(r)(c) > 0 => solving(grid, cell + 1)
      case (r, c) =>
        def cells(i: Int) = Seq(grid(r)(i), grid(i)(c), grid(s * (r / s) + i / s)(s * (c / s) + i % s))

        def guess(x: Int) = solving(grid.updated(r, grid(r).updated(c, x)), cell + 1)

        val used = grid.indices.flatMap(cells)
        (1 to GRID_SIZE).diff(used).collectFirst(Function.unlift(guess))
    }

    Future {
      solving(sudoku.g).map(new Sudoku(_)).get
    }
  }
}
