package ui

import java.awt.Font

import generator.Sudoku
import generator.levels.Level
import generator.levels.levelToString._
import solver.Solver

import scala.swing.{Action, Menu, MenuBar, MenuItem}

class SudokuMenuUI(sudoku: Sudoku, sudokuMainPanel: SudokuMainPanel, solver: Solver) extends MenuBar {

  contents += new Menu("File") {
    contents += new MenuItem(Action("Open sudoku") {
      println("Opening sudoku")
      sudoku.printout
    })
  }

  contents += new Menu("Generate") {
    contents += new MenuItem(Level.Easy)
    contents += new MenuItem(Level.Intermediate)
    contents += new MenuItem(Level.Hard)
  }

  contents += new Menu("Solver") {
    contents += new MenuItem(Action("Simple solver") {
      sudokuMainPanel.updateLabels(new Sudoku(solver.solve(sudoku.g)))
      sudokuMainPanel.revalidate()
    })
    contents += new MenuItem(Action("GA solver") {
      println("GA not implemented yet")
    })
  }

  menus.foreach {
    _.font = new Font(Font.MONOSPACED, Font.BOLD, 14)
  }
}
