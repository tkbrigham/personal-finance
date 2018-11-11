import sbt._

object CustomTasks {
  lazy val playConsole = taskKey[Unit]("Play sbt console") := {
    println("play console!")
  }

  def all: List[Def.Setting[Task[Unit]]] = List(playConsole)
}
