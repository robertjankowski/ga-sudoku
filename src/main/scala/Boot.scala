
import generator.Sudoku

import scala.util.Random

object Boot extends App {

  def printout(header: String, p: Array[Array[Int]]): Unit = {
    println(s"--- $header ---")
    p.foreach { row => println(s"${row.mkString("|")}") }
  }

  // create a possible solution
  val puzzle = new Sudoku(Array.fill(9, 9)(0)).a

  remove(puzzle, 60)

  printout("puzzle", puzzle)

  // solve the puzzle
  printout("solution", new Sudoku(puzzle).a)

  def remove(a: Array[Array[Int]], count: Int): Unit = {
    val rs = Random.shuffle(List.range(0, 81))
    for (i <- 0 until count)
      a(rs(i) / 9)(rs(i) % 9) = 0
  }
}