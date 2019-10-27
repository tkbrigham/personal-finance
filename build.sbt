name := "personal-finance"
organization := "com.thomas.brigham"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    CustomTasks.all
  )

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.postgresql" % "postgresql" % "42.2.8",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
)
