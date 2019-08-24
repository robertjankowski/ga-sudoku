package ga

import com.softwaremill.helisa._

object GA extends App {

  val INITIAL_POPULATION = 500
  val CROSSOVER_RATE = 0.1
  val MUTATION_RATE = 0.01

  // Example -> checking if Helisa is working
  case class Guess(num: Int)

  val Number = 42

  val genotype =
    () => genotypes.uniform(chromosomes.int(0, 100))
  def fitness(toGuess: Int) =
    (guess: Guess) => 1.0 / (guess.num - toGuess).abs

  val evolver =
    Evolver(fitness(Number), genotype)
      .populationSize(100)
      .phenotypeValidator(_.num % 2 == 0)
      .build()

  val stream = evolver.streamScalaStdLib()

  val best = stream.drop(1000).head.bestPhenotype

  println(best)


}
