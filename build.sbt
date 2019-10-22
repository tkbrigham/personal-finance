name := "personal-finance"
organization := "com.thomas.brigham"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    CustomTasks.all
  )

lazy val slickVersion = "3.2.2"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
)
