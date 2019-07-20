import generator.Sudoku
import generator.levels.Level

object Boot extends App {

  val sudoku = Sudoku(Level.Easy)
  sudoku.printout

  sudoku.toFile("sudoku_easy.txt")

  Sudoku.fromFile("sudoku_easy.txt").foreach {
    grid =>
      grid.g.foreach {
        row => println(s"${row.mkString(" ")}")
      }
  }

}