package solver

import generator.Sudoku
import generator.Sudoku._

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class SimpleSolver(implicit executionContext: ExecutionContext) extends Solver {

  private val rows: RowColumn = Array.fill(GRID_SIZE)(Set[Int]())
  private val columns: RowColumn = Array.fill(GRID_SIZE)(Set[Int]())
  private val boxes: Boxes = Array.fill(3, 3)(Set[Int]())

  override def solve(sudoku: Sudoku): Future[Sudoku] = {
    val s = new Sudoku(sudoku.getGrid)
    for {
      x <- 0 to 8
      y <- 0 to 8
      if (s.g(x)(y) != 0)
    } setExist(s.g(x)(y), x, y)

    def fill(x: Int, y: Int): Boolean = {
      if (s.g(x)(y) == 0) {
        var candidates = Set() ++ (1 to 9) -- rows(x) -- columns(y) -- boxes(x / 3)(y / 3)

        @tailrec
        def current(): Boolean = {
          if (candidates.isEmpty)
            false
          else {
            val v = Random.shuffle(candidates.toList).iterator.next
            candidates -= v
            s.g(x)(y) = v
            setExist(v, x, y)
            val good = if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
            if (good)
              true
            else {
              s.g(x)(y) = 0
              rows(x) -= v
              columns(y) -= v
              boxes(x / 3)(y / 3) -= v
              current()
            }
          }
        }

        current()
      }
      else if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
    }

    Future {
      fill(0, 0)
      s
    }
  }

  private def setExist(v: Int, x: Int, y: Int): Unit = {
    rows(x) += v
    columns(y) += v
    boxes(x / 3)(y / 3) += v
  }

}

