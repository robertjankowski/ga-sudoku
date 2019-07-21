package ui

import generator.Sudoku
import javax.swing.{LookAndFeel, UIManager, WindowConstants}
import solver.SimpleSolver

import scala.swing.BorderPanel.Position._
import scala.swing._

class SudokuUI(sudoku: Sudoku) extends Frame {
  title = "GA solving sudoku"
  preferredSize = new Dimension(Size.WIDTH, Size.HEIGHT)

  val mainPanel = new SudokuMainPanel(sudoku)
  menuBar = new SudokuMenuUI(sudoku, mainPanel, new SimpleSolver)

  contents = new BorderPanel {
    layout(mainPanel) = Center
  }
  centerOnScreen()
}

object SudokuUI {
  def create(sudoku: Sudoku, look: LookAndFeel): Unit = {
    UIManager.setLookAndFeel(look)
    val s = new SudokuUI(sudoku)
    s.peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    s.open()
  }
}

object Size {
  val WIDTH = 600
  val HEIGHT = 600
}