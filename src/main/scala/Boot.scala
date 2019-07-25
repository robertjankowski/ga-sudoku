import java.io.{File => JFile}

import generator.Sudoku
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import ui.SudokuUI

import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {

  Sudoku.fromFile(new JFile("sudoku_hard.txt")).foreach {
    SudokuUI.create(_, new NimbusLookAndFeel)
  }

}