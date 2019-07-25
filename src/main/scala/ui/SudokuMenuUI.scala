package ui

import java.awt.Font

import com.typesafe.scalalogging.LazyLogging
import generator.Sudoku
import generator.levels.Level
import generator.levels.levelToString._
import solver.Solver

import scala.concurrent.ExecutionContext
import scala.swing.{Action, FileChooser, Menu, MenuBar, MenuItem}
import scala.util.{Failure, Success}

class SudokuMenuUI(sudoku: Sudoku,
                   sudokuMainPanel: SudokuMainPanel,
                   solver: Solver)
                  (implicit executionContext: ExecutionContext) extends MenuBar with LazyLogging {

  contents += new Menu("File") {
    contents += new MenuItem(Action("Open sudoku") {
      val fc = new FileChooser()
      fc.showOpenDialog(null)
      Sudoku.fromFile(fc.selectedFile).foreach { s =>
        sudoku.changeGrid(s.g)
        sudokuMainPanel.updateLabels(s)
        sudokuMainPanel.revalidate()
      }
    })
  }

  contents += new Menu("Generate") {
    contents += new MenuItem(Level.Easy)
    contents += new MenuItem(Level.Intermediate)
    contents += new MenuItem(Level.Hard)
  }

  contents += new Menu("Solver") {
    contents += new MenuItem(Action("Simple solver") {
      solver.solve(sudoku.g).onComplete {
        case Failure(ex) => logger.error(s"Error occurs in solving sudoku: [${ex.getMessage}]")
        case Success(grid) =>
          sudokuMainPanel.updateLabels(new Sudoku(grid))
          sudokuMainPanel.revalidate()
      }
    })
    contents += new MenuItem(Action("GA solver") {
      logger.debug("GA not implemented yet")
    })
  }

  menus.foreach {
    _.font = new Font(Font.MONOSPACED, Font.BOLD, 14)
  }
}
