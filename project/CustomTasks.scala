import sbt._
import sbt.Keys._

object CustomTasks {
  lazy val playConsole = taskKey[Unit]("Play sbt console") := {
    println("play console!")
  }

  def all = List(
    playConsole
  )
}
