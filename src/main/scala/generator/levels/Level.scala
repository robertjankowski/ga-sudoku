package generator.levels

sealed trait Level

object Level {

  case object Easy extends Level

  case object Intermediate extends Level

  case object Hard extends Level

}
