lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.5"
lazy val jetty_server = "org.eclipse.jetty" % "jetty-server" % "9.3.3.v20150827"
lazy val jetty_servlet = "org.eclipse.jetty" % "jetty-servlet" % "9.3.3.v20150827"
lazy val jersey_core = "com.sun.jersey" % "jersey-core" % "1.19"
lazy val jersey_json = "com.sun.jersey" % "jersey-json" % "1.19"
lazy val jersey_servlet = "com.sun.jersey" % "jersey-servlet" % "1.19"
lazy val jersey_server = "com.sun.jersey" % "jersey-server" % "1.19"
lazy val fasterxml_jackson_module_scala =  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.1"
lazy val scala_csv = "com.github.tototoshi" %% "scala-csv" % "1.2.2"


lazy val commonSettings = Seq(
  organization := "org.myproject",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "NewGround test",
    
    mainClass in (Compile, run) := Some("com.newground.Runner"),
    
    libraryDependencies += jetty_server,
    libraryDependencies += jetty_servlet,
    libraryDependencies += jersey_core,
    libraryDependencies += jersey_server,
    libraryDependencies += jersey_servlet,
    libraryDependencies += jersey_json,
    libraryDependencies += fasterxml_jackson_module_scala,
    libraryDependencies += scala_csv,
    
    libraryDependencies += scalacheck % Test,
    
    EclipseKeys.withSource := true
  )
