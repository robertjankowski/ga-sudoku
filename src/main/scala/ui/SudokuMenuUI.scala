package ui

import java.awt.Font

import com.typesafe.scalalogging.LazyLogging
import generator.Sudoku
import generator.levels.Level
import generator.levels.Level._
import solver.{SimpleSolver, Solver}

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.{Action, FileChooser, Menu, MenuBar, MenuItem}
import scala.util.{Failure, Success}

class SudokuMenuUI(sudoku: Sudoku,
                   sudokuMainPanel: SudokuMainPanel)
                  (implicit executionContext: ExecutionContext) extends MenuBar with LazyLogging {

  val solver: Solver = new SimpleSolver()

  // TODO: issue with updating sudoku grid, after loading from file solver doesn't work
  val generateMenu = Menus.GenerateMenu(sudokuMainPanel)
  val solverMenu = Menus.SolverMenu(solver, sudokuMainPanel)
  val fileMenu = Menus.FileMenu(sudokuMainPanel)

  contents ++= Seq(fileMenu, generateMenu, solverMenu)

  menus.foreach {
    _.font = new Font(Font.MONOSPACED, Font.BOLD, 14)
  }
}

object Menus {

  class FileMenu(sudokuMainPanel: SudokuMainPanel)
                (implicit executionContext: ExecutionContext) extends Menu("File") {

    contents += new MenuItem(Action("Open sudoku") {
      val fc = new FileChooser()
      fc.showOpenDialog(null)
      Sudoku.fromFile(fc.selectedFile).foreach { s =>
        sudokuMainPanel.setSudokuLabels(s)
        sudokuMainPanel.revalidate()
      }
    })
  }

  object FileMenu {
    def apply(sudokuMainPanel: SudokuMainPanel)
             (implicit executionContext: ExecutionContext): Menu =
      new FileMenu(sudokuMainPanel)
  }

  class GenerateMenu(sudokuMainPanel: SudokuMainPanel)
                    (implicit executionContext: ExecutionContext) extends Menu("Generate sudoku") {
    contents += new MenuItem(Action(Level.Easy) {
      setSudoku(Level.Easy)
    })
    contents += new MenuItem(Action(Level.Intermediate) {
      setSudoku(Level.Intermediate)
    })
    contents += new MenuItem(Action(Level.Hard) {
      setSudoku(Level.Hard)
    })

    private def setSudoku(level: Level): Future[Unit] = {
      Sudoku(level).map { sudoku =>
        sudokuMainPanel.setSudokuLabels(sudoku)
        sudokuMainPanel.revalidate()
        sudokuMainPanel.s.printout()
      }
    }
  }

  object GenerateMenu {
    def apply(sudokuMainPanel: SudokuMainPanel)
             (implicit executionContext: ExecutionContext): Menu =
      new GenerateMenu(sudokuMainPanel)
  }

  class SolverMenu(solver: Solver,
                   sudokuMainPanel: SudokuMainPanel)
                  (implicit executionContext: ExecutionContext) extends Menu("Solve") with LazyLogging {

    contents += new MenuItem(Action("Simple solver") {
      solver.solve(sudokuMainPanel.s).onComplete {
        case Failure(ex) => logger.error(s"Error occurs in solving sudoku", ex)
        case Success(sudoku) =>
          sudokuMainPanel.setSudokuLabels(sudoku)
          sudokuMainPanel.revalidate()
      }
    })
    contents += new MenuItem(Action("GA solver") {
      logger.debug("GA not implemented yet")
    })
  }

  object SolverMenu {
    def apply(solver: Solver,
              sudokuMainPanel: SudokuMainPanel)
             (implicit executionContext: ExecutionContext): Menu =
      new SolverMenu(solver, sudokuMainPanel)
  }

}
