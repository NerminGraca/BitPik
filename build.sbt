name := """bitpik"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "com.typesafe.play" %% "play-mailer" % "2.4.0",
  "com.paypal.sdk" % "rest-api-sdk" % "1.2.0",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.apache.poi" % "poi" % "3.11"

)

