package ga

import generator.Sudoku
import generator.Sudoku.Grid
import solver.Solver

import scala.concurrent.Future
import scala.util.Random

class GA(solution: Grid) extends Solver {

  private implicit val r = new Random()

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

      // Mutation

      // Add fittest offspring to population and remove the worst individual from previous generation
      addFittestOffspring()

      // Calculate fitness
      population.foreach(_.updateFitness())
      fitness = population.map(_.fitness).sum
      generation -= 1
    }
    // TODO: pack this function into future not to block UI
    Future.successful(new Sudoku(getFittest.grid))
  }

  private def selection(): Unit = {
    fittest = getFittest
    secondFittest = getSecondFittest
  }

  private def crossover(): Unit = {
    // TODO:
    //  1. select `position` (either row or column or (3x3 grid))
    //  2. select random number 1 to 9 and change parents `position`
  }

  private def getFittest = population.minBy(_.fitness)

  private def getSecondFittest = population.sortBy(_.fitness).drop(1).head

  private def getFittestOffspring =
    if (fittest.fitness > secondFittest.fitness) fittest else secondFittest

  private def addFittestOffspring() = {
    fittest.updateFitness()
    secondFittest.updateFitness()

    // get least index of least fit individual
    val worstIndex = population.indexOf(population.minBy(_.fitness))
    population.updated(worstIndex, getFittestOffspring)
  }
}

object GA {
  val POPULATION_SIZE = 500
  val MAX_GENERATIONS = 100
  val CROSSOVER_RATE = 0.1
  val MUTATION_RATE = 0.01
}
