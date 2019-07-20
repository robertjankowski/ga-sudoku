package generator

import java.io.{File, FileWriter}

import com.typesafe.scalalogging.LazyLogging
import generator.Sudoku._
import generator.levels.Level
import generator.levels.Level.{Easy, Hard, Intermediate}
import solver.SimpleSolver

import scala.io.Source
import scala.util.{Random, Try, Using}

class Sudoku(val g: Grid) extends LazyLogging {

  def printout: Unit = {
    println("+" + "---+" * 9)
    for ((row, i) <- g.zipWithIndex) {
      val r = row.map { v =>
        if (v != 0) v.toString else " "
      }
      print("|")
      for (j <- 0 until 3)
        print(s" ${r(j)}   ${r(j + 1)}   ${r(j + 2)} |")
      println()
      if (i % 3 == 2)
        println("+" + "---+" * 9)
      else
        println("+" + "   +" * 9)
    }
  }

  def toFile(fileName: String, delimiter: String = " "): Try[Unit] = {
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

}

object Sudoku {
  type Grid = Array[Array[Int]]
  type Boxes = Array[Array[Set[Int]]]
  type RowColumn = Array[Set[Int]]

  def apply(level: Level): Sudoku = {
    val grid = new SimpleSolver().solve(Array.fill(9, 9)(0))
    val sudoku = new Sudoku(grid)
    level match {
      case Easy => remove(sudoku.g, 20)
      case Intermediate => remove(sudoku.g, 40)
      case Hard => remove(sudoku.g, 60)
    }
    sudoku
  }

  private def remove(a: Grid, count: Int): Unit = {
    val rs = Random.shuffle(List.range(0, 81))
    for (i <- 0 until count)
      a(rs(i) / 9)(rs(i) % 9) = 0
  }


  def fromFile(fileName: String): Try[Sudoku] =
    Using(Source.fromFile(fileName)) { source =>
      source.getLines()
        .map {
          _.split(" ").map(_.trim.toInt)
        }
        .toArray
    }.map(new Sudoku(_))

}