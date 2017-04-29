name := "scala-hoots"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.5.0"
val slf4jVersion = "1.7.21"
val logBackVersion = "1.1.7"
val scalaLoggingVersion = "3.5.0"

val slf4jApi = "org.slf4j" % "slf4j-api" % slf4jVersion
val logBackClassic = "ch.qos.logback" % "logback-classic" % logBackVersion
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
val loggingStack = Seq(slf4jApi, logBackClassic, scalaLogging)

val typesafeConfig = "com.typesafe" % "config" % "1.3.1"

val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
val akkaStack = Seq(akka, akkaTestKit, akkaStream, akkaStreamTestKit)

libraryDependencies += typesafeConfig
libraryDependencies ++= akkaStack ++ loggingStack

// https://mvnrepository.com/artifact/com.github.rwl/jtransforms
libraryDependencies += "com.github.rwl" % "jtransforms" % "2.4.0"

// https://mvnrepository.com/artifact/com.googlecode.soundlibs/mp3spi
libraryDependencies += "com.googlecode.soundlibs" % "mp3spi" % "1.9.5-1"

// https://mvnrepository.com/artifact/com.googlecode.soundlibs/jlayer
libraryDependencies += "com.googlecode.soundlibs" % "jlayer" % "1.0.1.4"

// https://mvnrepository.com/artifact/com.googlecode.soundlibs/tritonus-all
libraryDependencies += "com.googlecode.soundlibs" % "tritonus-all" % "0.3.7.2"
