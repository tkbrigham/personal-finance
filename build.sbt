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
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
  "org.postgresql" % "postgresql" % "42.2.8",
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.4.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.4.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.7.0-scalikejdbc-3.4",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.7.0-scalikejdbc-3.4"
)
