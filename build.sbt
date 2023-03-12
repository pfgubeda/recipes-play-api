name := """proyecto_clase"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

enablePlugins(PlayEbean)
libraryDependencies += evolutions
libraryDependencies += jdbc
libraryDependencies += guice

libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.5"
libraryDependencies += "org.fusesource.jansi" % "jansi" % "1.18"

libraryDependencies ++= Seq(
  ehcache
)
