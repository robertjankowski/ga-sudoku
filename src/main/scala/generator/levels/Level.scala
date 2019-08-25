package generator.levels

sealed trait Level

object Level {

  case object Easy extends Level
  case object Intermediate extends Level
  case object Hard extends Level

  implicit def levelToString(level: Level): String = level match {
    case Easy => "Easy"
    case Intermediate => "Intermediate"
    case Hard => "Hard"
  }

  implicit class LevelOps(val level: Level) extends AnyVal {
    def getBlocksNumber: Int = level match {
      case Easy => 20
      case Intermediate => 40
      case Hard => 60
    }
  }
}