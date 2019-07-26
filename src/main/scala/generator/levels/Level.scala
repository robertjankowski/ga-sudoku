package generator.levels

sealed trait Level {
  def removal: Int
}

object Level {

  case object Easy extends Level {
    override def removal: Int = 20
  }

  case object Intermediate extends Level {
    override def removal: Int = 40
  }

  case object Hard extends Level {
    override def removal: Int = 60
  }

  implicit def levelToString(level: Level): String = level match {
    case Easy => "Easy"
    case Intermediate => "Intermediate"
    case Hard => "Hard"
  }
}