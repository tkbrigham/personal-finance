name := "personal-finance"
organization := "com.thomas.brigham"

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    CustomTasks.all
  )

scalaVersion := "2.12.7"

lazy val slickVersion = "3.2.2"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
  "com.liyaos" %% "scala-forklift-slick" % "0.3.1",
  guice
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.thomas.brigham.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.thomas.brigham.binders._"
