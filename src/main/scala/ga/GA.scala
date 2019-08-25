package ga

import generator.Sudoku
import generator.Sudoku.Grid
import solver.Solver

import scala.concurrent.Future
import scala.util.Random

class GA(solution: Grid) extends Solver {

  private val r = new Random()
  private val population: List[Individual] = List.fill(GA.POPULATION_SIZE)(new Individual(solution))
  var fittest = new Individual(solution)
  var secondFittest = new Individual(solution)
  var generation: Int = GA.MAX_GENERATIONS

  override def solve(sudoku: Sudoku): Future[Sudoku] = {
    var fitness: Double = 0
    // Calculate every individual in population
    population.foreach(_.updateFitness())

    while (generation > 0 || fitness < 1) {

      selection()

      crossover()

      if (r.nextDouble() < GA.MUTATION_RATE)
        mutation()

      // Add fittest offspring to population and remove the worst individual from previous generation
      addFittestOffspring()

      // Calculate fitness
      population.foreach(_.updateFitness())
      fitness = population.map(_.fitness).sum
      generation -= 1
      println(s"Fitness: ${fittest.fitness} | second: ${secondFittest.fitness}")
    }

    println()
    // TODO: pack this function into future not to block UI
    val s = new Sudoku(getFittest.ownGrid)
    println(s.toPrettyPrint)
    println()
    println(sudoku.toPrettyPrint)
    Future.successful(s)
  }

  private def getFittest = population.minBy(_.fitness)

  private def getSecondFittest = population.sortBy(_.fitness).drop(1).head

  private def getFittestOffspring =
    if (fittest.fitness > secondFittest.fitness) fittest else secondFittest

  private def selection(): Unit = {
    fittest = getFittest
    secondFittest = getSecondFittest
  }

  private def crossover(): Unit = {
    //  1. select `position` (either row or column or (3x3 grid))
    //  2. select random number 1 to 9 and change parents `position`
    val position = r.nextInt(9) // row/column/small_grid position
    r.nextInt(3) match {
      case 0 => crossRows(fittest, secondFittest, position)
      case 1 => crossColumns(fittest, secondFittest, position)
      case 2 => crossSmallGrids(fittest, secondFittest, position)
    }
  }

  private def mutation(): Unit = {
    // randomly change position of numbers of the fittest individual in row/column/small_grid
    val pos1 = r.nextInt(9)
    val pos2 = r.nextInt(9)
    val position = r.nextInt(9)
    r.nextInt(3) match {
      case 0 => mutateRow(fittest, position, pos1, pos2)
      case 1 => mutateCol(fittest, position, pos1, pos2)
      case 2 => mutateSmallGrid(fittest, position, pos1, pos2)
    }
  }

  private def addFittestOffspring() = {
    fittest.updateFitness()
    secondFittest.updateFitness()

    // get least index of least fit individual
    val worstIndex = population.indexOf(population.minBy(_.fitness))
    population.updated(worstIndex, getFittestOffspring)
  }

  private def crossRows(id1: Individual, id2: Individual, position: Int): Unit = {
    val row1 = id1.ownGrid(position)
    val row2 = id2.ownGrid(position)

    id1.ownGrid(position) = row2
    id2.ownGrid(position) = row1
  }

  private def crossColumns(id1: Individual, id2: Individual, position: Int): Unit = {
    id1.transposeGrid()
    id2.transposeGrid()
    crossRows(id1, id2, position)
    id1.transposeGrid()
    id2.transposeGrid()
  }

  private def crossSmallGrids(id1: Individual, id2: Individual, position: Int): Unit = {
    val smallGrid1 = Sudoku.createBoxes(id1.ownGrid)
    val smallGrid2 = Sudoku.createBoxes(id2.ownGrid)

    // update grid of individuals to boxes

    // crossRow

    // return to initial grid
  }


  private def mutateRow(individual: Individual, position: Int, pos1: Int, pos2: Int): Unit = {
    val x = individual.ownGrid(position)(pos1)
    val y = individual.ownGrid(position)(pos2)
    individual.ownGrid(position)(pos1) = y
    individual.ownGrid(position)(pos2) = x
  }

  private def mutateCol(individual: Individual, position: Int, pos1: Int, pos2: Int): Unit = {
    individual.transposeGrid()
    mutateRow(individual, position, pos1, pos2)
    individual.transposeGrid()
  }

  private def mutateSmallGrid(individual: Individual, position: Int, pos1: Int, pos2: Int): Unit = {
    // TODO
  }

}

object GA {
  val POPULATION_SIZE = 500
  val MAX_GENERATIONS = 2000
  val MUTATION_RATE = 0.4
}
