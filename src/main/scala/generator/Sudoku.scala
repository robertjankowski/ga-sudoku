package generator

import generator.Sudoku._

import scala.annotation.tailrec
import scala.util.Random

class Sudoku(val a: Grid) {

  val rows: RowColumn = Array.fill(9)(Set[Int]())
  val columns: RowColumn = Array.fill(9)(Set[Int]())
  val boxes: Boxes = Array.fill(3, 3)(Set[Int]())

  for {
    x <- 0 to 8
    y <- 0 to 8
  }
    if (a(x)(y) != 0)
      setExist(a(x)(y), x, y)

  private def setExist(v: Int, x: Int, y: Int): Unit = {
    rows(x) += v
    columns(y) += v
    boxes(x / 3)(y / 3) += v
  }

  def fill(x: Int, y: Int): Boolean = {
    if (a(x)(y) == 0) {
      var candidates = Set() ++ (1 to 9) -- rows(x) -- columns(y) -- boxes(x / 3)(y / 3)

      @tailrec
      def current(): Boolean = {
        if (candidates.isEmpty)
          false
        else {
          val v = Random.shuffle(candidates.toList).iterator.next
          candidates -= v
          a(x)(y) = v
          setExist(v, x, y)
          val good = if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
          if (good)
            true
          else {
            a(x)(y) = 0
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

  fill(0, 0)
}

object Sudoku {
  type Grid = Array[Array[Int]]
  type Boxes = Array[Array[Set[Int]]]
  type RowColumn = Array[Set[Int]]
}