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
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
  guice,
  jdbc,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.thomas.brigham.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.thomas.brigham.binders._"
