package generator

import java.io.{File, FileWriter}

import com.typesafe.scalalogging.LazyLogging
import generator.Sudoku._
import generator.levels.Level
import solver.SimpleSolver

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util._

class Sudoku(var g: Grid)(implicit executionContext: ExecutionContext) extends LazyLogging {

  def printout(): Unit = {
    println("+" + "---+" * GRID_SIZE)
    for ((row, i) <- g.zipWithIndex) {
      val r = row.map { v =>
        if (v != 0) v.toString else " "
      }
      print("|")
      for (j <- 0 until 3)
        print(s" ${r(j)}   ${r(j + 1)}   ${r(j + 2)} |")
      println()
      if (i % 3 == 2)
        println("+" + "---+" * GRID_SIZE)
      else
        println("+" + "   +" * GRID_SIZE)
    }
  }

  def toFile(fileName: String)(delimiter: String = " "): Try[Unit] = {
    val grid: String = g.map { row => row.mkString(delimiter) }.mkString("\n")
    val writer = Try(new FileWriter(new File(fileName)))
    writer
      .map {
        w => w.write(grid); w
      }
      .recoverWith {
        case ex: Exception =>
          logger.error(s"Error in saving to file:\n${ex.getMessage}")
          writer
      }
      .map(_.close())
  }

  def changeGrid(newGrid: Grid): Unit = this.g = newGrid

  def getGrid: Grid = g
}

object Sudoku {
  type Grid = Array[Array[Int]]
  type Boxes = Array[Array[Set[Int]]]
  type RowColumn = Array[Set[Int]]
  val GRID_SIZE = 9

  def apply(level: Level)(implicit executionContext: ExecutionContext): Future[Sudoku] = {
    val sudoku = new Sudoku(Array.fill(GRID_SIZE, GRID_SIZE)(0))
    new SimpleSolver().solve(sudoku).map { sudoku =>
      remove(sudoku.g, level.removal)
      sudoku
    }
  }

  def fromFile(fileName: File)(implicit executionContext: ExecutionContext): Try[Sudoku] =
    Using(Source.fromFile(fileName)) { source =>
      source.getLines()
        .map {
          _.split(" ").map(_.trim.toInt)
        }
        .toArray
    }.map(new Sudoku(_))

  private def remove(a: Grid, count: Int): Unit = {
    val rs = Random.shuffle(List.range(0, GRID_SIZE * GRID_SIZE))
    for (i <- 0 until count)
      a(rs(i) / 9)(rs(i) % 9) = 0
  }

}