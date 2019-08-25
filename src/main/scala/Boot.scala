import generator.Sudoku
import generator.levels.Level
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import ui.SudokuUI

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {

  val sudokuUI = Sudoku(Level.Easy).map { sudoku =>
    SudokuUI.create(sudoku, new NimbusLookAndFeel)
  }
  Await.ready(sudokuUI, 2 seconds)
}