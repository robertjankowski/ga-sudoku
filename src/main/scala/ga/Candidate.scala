package ga

import generator.Sudoku.Grid
import generator.Sudoku.GRID_SIZE

import scala.util.Random

class Candidate(implicit random: Random) {

  private var grid: Grid = Array.fill(GRID_SIZE, GRID_SIZE)(0)

  def fitness(): Unit = {
    Fitness.calculateFitness(grid)
  }

  def mutate(mutationRate: Double)= {
  }


}
