import ga.GA
import generator.Sudoku
import generator.levels.Level

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {

  //  val sudokuUI = Sudoku(Level.Easy).map { sudoku =>
  //    SudokuUI.create(sudoku, new NimbusLookAndFeel)
  //  }
  //  Await.ready(sudokuUI, 2 seconds)

  val sudoku = Await.result(Sudoku(Level.Easy), 1 second)
  println(sudoku.toPrettyPrint)
  val ga = new GA(sudoku.g)
  val solvedSudoku = ga.solve(sudoku)
  Await.ready(solvedSudoku, 10 seconds)
}