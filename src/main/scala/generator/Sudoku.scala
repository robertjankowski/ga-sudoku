package generator

import java.io.{File => JFile, FileWriter => JFileWriter}

import com.typesafe.scalalogging.LazyLogging
import generator.Sudoku._
import generator.levels.Level
import solver.SimpleSolver
import utils.Using.using

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util._

class Sudoku(var g: Grid) extends LazyLogging {

  def printOneLine(): Unit = {
    println(s"Sudoku ${g.map(_.mkString("")).mkString("")}")
  }

  def toPrettyPrint: String = {
    g grouped 3 map {
      _ map {
        _ grouped 3 map {
          _ mkString " "
        } mkString " | "
      } mkString "\n"
    } mkString s"\n${"-" * 11 mkString " "}\n"
  }

  def toFile(fileName: String)(delimiter: String = " "): Try[Unit] = {
    val grid: String = g.map {
      row => row.mkString(delimiter)
    }.mkString("\n")
    val writer = Try(new JFileWriter(new JFile(fileName)))
    writer
      .map {
        w =>
          w.write(grid);
          w
      }
      .recoverWith {
        case ex: Exception =>
          logger.error(s"Error in saving to file:\n${ex.getMessage}")
          writer
      }
      .map(_.close())
  }
}

object Sudoku {
  type Grid = Array[Array[Int]]
  val GRID_SIZE = 9

  def apply(level: Level)(implicit executionContext: ExecutionContext): Future[Sudoku] = {
    val sudoku = new Sudoku(Array.fill(GRID_SIZE, GRID_SIZE)(0))
    new SimpleSolver().solve(sudoku).map { sudoku =>
      remove(sudoku.g, level.getBlocksNumber)
      new SudokuGenerator(sudoku.g).generate()
    }
  }

  def fromFile(fileName: JFile)(implicit executionContext: ExecutionContext): Try[Sudoku] = {
    using(Source.fromFile(fileName)) { source =>
      source.getLines()
        .map {
          _.split(" ").map(_.trim.toInt)
        }
        .toArray
    }.map(new Sudoku(_))
  }

  def createBoxes(grid: Grid): Grid = {
    var boxes = Array.empty[Array[Int]]
    for {
      r <- 0 until 3
      c <- 0 until 3
    } {
      var b = Array.empty[Int]
      for {
        i <- 0 until 3
        j <- 0 until 3
      } b +:= grid(3 * r + i)(3 * c + j)
      boxes +:= b
    }
    boxes
  }

  def boxesToGrid(grid: Grid): Grid = {
    val g = ArrayBuffer.empty[Array[Int]]
    for {
      i <- 2 until 9 by 3
      j <- 2 until 9 by 3
    } {
      var smallGrid = ArrayBuffer.empty[Int]
      for {
        k <- 2 to 0 by -1
        l <- 2 to 0 by -1
      } {
        smallGrid += grid(i - k)(j - l)
      }
      g += smallGrid.reverse.toArray
    }
    g.toArray
  }

  private def remove(a: Grid, count: Int): Unit = {
    val rs = Random.shuffle(List.range(0, GRID_SIZE * GRID_SIZE))
    for (i <- 0 until count)
      a(rs(i) / GRID_SIZE)(rs(i) % GRID_SIZE) = 0
  }

}