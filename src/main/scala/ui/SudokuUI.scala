package ui

import java.awt.Color

import javax.swing.UIManager
import javax.swing.plaf.nimbus.NimbusLookAndFeel

import scala.swing.BorderPanel.Position._
import scala.swing._

class SudokuUI extends Frame {

  UIManager.setLookAndFeel(new NimbusLookAndFeel)

  title = "GA solving sudoku"
  size = new Dimension(200, 200)

  val button = new Button {
    text = "Start solving sudoku"
    foreground = Color.BLACK
  }
  contents = new BorderPanel {
    layout(button) = North
  }
  menuBar = new MenuBar {
    contents += new Menu("Sudoku") {
      contents += new MenuItem(Action("Open") {
        println("Opening sudoku")
      })
      contents += new MenuItem(Action("Generate") {
        println("Generating sudoku")
      })
    }
  }
  // pack()
  centerOnScreen()
}

object SudokuUI {
  def create: Unit = {
    new SudokuUI()
      .open()
  }
}
