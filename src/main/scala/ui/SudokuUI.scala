package ui

import java.util.concurrent.Executors

import generator.Sudoku
import javax.swing.{LookAndFeel, UIManager, WindowConstants}

import scala.concurrent.ExecutionContext
import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.util.Try

class SudokuUI(sudoku: Sudoku) extends Frame {

  private implicit val ec = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  title = "GA solving sudoku"
  preferredSize = new Dimension(Size.WIDTH, Size.HEIGHT)

  val mainPanel = new SudokuMainPanel(sudoku)
  menuBar = new SudokuMenuUI(sudoku, mainPanel)

  contents = new BorderPanel {
    layout(mainPanel) = Center
  }
  centerOnScreen()
}

object SudokuUI {
  def create(sudoku: Sudoku, look: LookAndFeel): Unit =
    Try { new SudokuUI(sudoku) }.foreach { s =>
      UIManager.setLookAndFeel(look)
      s.peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      s.open()
    }
}

object Size {
  val WIDTH = 600
  val HEIGHT = 600
}