package solver

import generator.Sudoku.{Boxes, Grid, RowColumn}

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class SimpleSolver(implicit executionContext: ExecutionContext) extends Solver {

  private val rows: RowColumn = Array.fill(9)(Set[Int]())
  private val columns: RowColumn = Array.fill(9)(Set[Int]())
  private val boxes: Boxes = Array.fill(3, 3)(Set[Int]())

  override def solve(sudoku: Grid): Future[Grid] = {
    for {
      x <- 0 to 8
      y <- 0 to 8
      if (sudoku(x)(y) != 0)
    } setExist(sudoku(x)(y), x, y)

    def fill(x: Int, y: Int): Boolean = {
      if (sudoku(x)(y) == 0) {
        var candidates = Set() ++ (1 to 9) -- rows(x) -- columns(y) -- boxes(x / 3)(y / 3)

        @tailrec
        def current(): Boolean = {
          if (candidates.isEmpty)
            false
          else {
            val v = Random.shuffle(candidates.toList).iterator.next
            candidates -= v
            sudoku(x)(y) = v
            setExist(v, x, y)
            val good = if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
            if (good)
              true
            else {
              sudoku(x)(y) = 0
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
      fill(0, 0);
      sudoku
    }
  }

  private def setExist(v: Int, x: Int, y: Int): Unit = {
    rows(x) += v
    columns(y) += v
    boxes(x / 3)(y / 3) += v
  }

}

