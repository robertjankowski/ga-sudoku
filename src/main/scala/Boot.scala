import generator.Sudoku
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import ui.SudokuUI

object Boot extends App {

  Sudoku.fromFile("sudoku_easy.txt").foreach {
    SudokuUI.create(_, new NimbusLookAndFeel)
  }

}