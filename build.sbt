version := "0.1.1"

scalaVersion := "2.13.2"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.3.0"

lazy val hello = (project in file("."))
  .settings(
    name := "GamyMcGameFace",
    fork in run := true,
    javaOptions in run ++= Seq("-XstartOnFirstThread", "-Djava.library.path=lib"),
    scalacOptions += "-Xfatal-warnings",
    scalacOptions += "-deprecation"
  )
