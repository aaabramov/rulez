name := "one-tine-link"
organization := "com.aaabramov"
version := "1.0.0"

enablePlugins(PlayScala)

scalaVersion := "2.12.11"
scalacOptions += "-Ypartial-unification"
// compile
libraryDependencies ++= Seq(
  guice,
  "org.postgresql" % "postgresql" % "42.2.12",
  "com.github.tminglei" %% "slick-pg" % "0.19.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.19.0",
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.typelevel" %% "cats-core" % "2.0.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "4.1",
  "com.pauldijou" %% "jwt-play" % "4.2.0"
)

// test
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0"
).map(_ % Test)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.aaabramov.controllers._"

// Adds additional packages into conf/routes
play.sbt.routes.RoutesKeys.routesImport += "java.util.UUID"
