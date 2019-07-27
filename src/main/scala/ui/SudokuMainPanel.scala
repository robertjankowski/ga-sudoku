package ui

import java.awt.{Color, Font}

import generator.Sudoku
import generator.Sudoku.GRID_SIZE

import scala.swing.{GridPanel, Label, Swing}

class SudokuMainPanel(sudoku: Sudoku) extends GridPanel(GRID_SIZE, GRID_SIZE) {

  var s: Sudoku = sudoku
  setSudokuLabels(s)
  border = Swing.LineBorder(Color.BLACK)

  def setSudokuLabels(sudoku: Sudoku): Unit = {
    contents.clear()
    s = sudoku
    val labels = sudokuToLabel(s)
    addBorderToLabels(labels)
    labels.foreach(contents += _)
    labels.foreach(l => print(l.text))
    println()
  }

  def updateSudoku(sudoku: Sudoku): Unit = {
    setSudokuLabels(sudoku)
    this.revalidate()
  }

  private def sudokuToLabel(sudoku: Sudoku): Array[Label] =
    for {
      rows <- sudoku.g
      row <- rows.toList
    } yield {
      val num = if (row != 0) row.toString else " "
      val l = new Label(num)
      l.font = new Font(Font.MONOSPACED, Font.BOLD, 20)
      l
    }

  private def addBorderToLabels(labels: Array[Label]): Unit =
    for (i <- labels.indices) {
      if (i / 9 == 3 || i < 9 || i / 9 == 6) {
        labels(i).border = Swing.MatteBorder(2, 0, 0, 0, Color.BLACK)
        if (i % 3 == 2) {
          labels(i).border = Swing.MatteBorder(2, 0, 0, 2, Color.BLACK)
        }
      }
      else if (i % 3 == 2) {
        labels(i).border = Swing.MatteBorder(0, 0, 0, 2, Color.BLACK)
      }
    }
}