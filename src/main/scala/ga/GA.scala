package ga

import generator.Sudoku
import solver.Solver

import scala.concurrent.Future
import scala.util.Random

class GA(solution: Sudoku) extends Solver {

  private val r = new Random()

  private type Population = List[Individual]
  private var oldPopulation = getPopulation(GA.POPULATION_SIZE)
  private var newPopulation = getPopulation(GA.POPULATION_SIZE)

  private var totalFitness: Double = 0
  private var nGeneration = 0

  // Number of mutations and crossovers
  private var nMutation = 0
  private var nCross = 0

  // Statistics
  private var average: Double = 0
  private var max: Double = 0
  private var min: Double = 0

  override def solve(sudoku: Sudoku): Future[Sudoku] = {

    while (nGeneration <= GA.POPULATION_SIZE) {
      nGeneration += 1

      generation()

      statistics(GA.POPULATION_SIZE, newPopulation)
      report(nGeneration)
      oldPopulation = newPopulation
    }
    println(solution.toPrettyPrint)
    println()
    val s = new Sudoku(newPopulation.maxBy(_.fitness).ownGrid)
    println(s.toPrettyPrint)
    Future.successful(s)
  }

  private def getPopulation(populationSize: Int): Population = List.fill(populationSize)(new Individual(solution.g))

  private def select(populationSize: Int, totalFitness: Double, population: Population): Int = {
    // Select a single individual via roulette wheel selection
    val rand = r.nextDouble() * totalFitness
    var partSum = 0.0
    var acc = 0
    while (acc < (populationSize - 1) && partSum < rand) {
      acc += 1
      partSum += population(acc).fitness
    }
    acc
  }

  private def crossoverWitMutation(parent1: Individual,
                                   parent2: Individual,
                                   child1: Individual,
                                   child2: Individual): Unit = {
    if (r.nextDouble() < GA.CROSSOVER_RATE) {
      // TODO: fix crossover -> Now it's possible that there will be more that 9 e.g 1s
      crossover(parent1, parent2, child1, child2)
      nCross += 1
    }
    // TODO: finish mutation
    mutation(parent1, GA.MUTATION_RATE)
    mutation(parent2, GA.MUTATION_RATE)
    mutation(child1, GA.MUTATION_RATE)
    mutation(child2, GA.MUTATION_RATE)
  }

  private def generation(): Unit = {
    var pSize = 0
    while (pSize < (GA.POPULATION_SIZE - 1)) {
      val mate1 = select(GA.POPULATION_SIZE, totalFitness, oldPopulation)
      val mate2 = select(GA.POPULATION_SIZE, totalFitness, oldPopulation)

      crossoverWitMutation(oldPopulation(mate1), oldPopulation(mate2), newPopulation(pSize), newPopulation(pSize + 1))

      newPopulation(pSize).updateFitness()
      newPopulation(pSize + 1).updateFitness()

      pSize += 2
    }
  }

  private def statistics(populationSize: Int, population: List[Individual]): Unit = {
    totalFitness = population.map(_.fitness).sum
    min = population.minBy(_.fitness).fitness
    max = population.maxBy(_.fitness).fitness
    average = totalFitness / populationSize
  }

  private def report(generationNumber: Int): Unit = {
    println(s"Population Report | Generation $generationNumber")
    println(s"> Accumulated stats: max=$max | min=$min | average=$average | " +
      s"sum=$totalFitness | nmutation=$nMutation | ncross = $nCross")
  }

  private def crossover(parent1: Individual, parent2: Individual, child1: Individual, child2: Individual) = {
    //  1. select `position` (either row or column or (3x3 grid))
    //  2. select random number 1 to 9 and change parents `position`
    val position = r.nextInt(9) // row/column/small_grid position
    r.nextInt(3) match {
      case 0 => crossRows(parent1, parent2, child1, child2, position)
      case 1 => crossColumns(parent1, parent2, child1, child2, position)
      case 2 => crossSmallGrids(parent1, parent2, child1, child2, position)
    }
  }

  private def mutation(individual: Individual, mutationRate: Double): Unit = {
    // randomly change position of numbers of the fittest individual in row/column/small_grid
    if (r.nextDouble() < mutationRate) {
      val pos1 = r.nextInt(9)
      val pos2 = r.nextInt(9)
      val position = r.nextInt(9)
      r.nextInt(3) match {
        case 0 => mutateRow(individual, position, pos1, pos2)
        case 1 => mutateCol(individual, position, pos1, pos2)
        case 2 => mutateSmallGrid(individual, position, pos1, pos2)
      }
      nMutation += 1
    }
  }

  private def crossRows(parent1: Individual,
                        parent2: Individual,
                        child1: Individual,
                        child2: Individual,
                        position: Int) = {
    val row1 = parent1.ownGrid(position)
    val row2 = parent2.ownGrid(position)

    child1.ownGrid(position) = row2
    child2.ownGrid(position) = row1
  }

  private def crossColumns(parent1: Individual,
                           parent2: Individual,
                           child1: Individual,
                           child2: Individual,
                           position: Int) = {
    parent1.transposeGrid()
    parent1.transposeGrid()
    crossRows(parent1, parent2, child1, child2, position)
    parent1.transposeGrid()
    parent2.transposeGrid()
  }

  private def crossSmallGrids(parent1: Individual,
                              parent2: Individual,
                              child1: Individual,
                              child2: Individual,
                              position: Int) = {
    parent1.ownGrid = Sudoku.createBoxes(parent1.ownGrid)
    parent2.ownGrid = Sudoku.createBoxes(parent1.ownGrid)
    crossRows(parent1, parent2, child1, child2, position)
    parent1.ownGrid = Sudoku.boxesToGrid(parent1.ownGrid)
    parent2.ownGrid = Sudoku.boxesToGrid(parent2.ownGrid)
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
    individual.ownGrid = Sudoku.createBoxes(individual.ownGrid)
    mutateRow(individual, position, pos1, pos2)
    individual.ownGrid = Sudoku.boxesToGrid(individual.ownGrid)
  }

}

object GA {
  val POPULATION_SIZE = 500
  val MAX_GENERATIONS = 4000
  val MUTATION_RATE = 0.8
  val CROSSOVER_RATE = 0.8
}
