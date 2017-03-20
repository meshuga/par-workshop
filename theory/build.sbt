name := "theory"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2"
libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "io.vertx" % "vertx-core" % "3.4.0"
libraryDependencies += "io.vertx" % "vertx-web" % "3.4.0"
libraryDependencies += "io.vertx" % "vertx-rx-java" % "3.4.0"
libraryDependencies += "io.vertx" % "vertx-jdbc-client" % "3.4.0"
libraryDependencies += "org.hsqldb" % "hsqldb" % "2.3.4"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.24"