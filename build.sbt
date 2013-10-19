name := "FinStmt DSL"

version := "0.0.1"

organization := "com.begley"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "http://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "http://oss.sonatype.org/content/repositories/releases"
                 )





scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.6.0"

    libraryDependencies ++= Seq(
    "junit" % "junit" % "4.8.1" % "test"
    )



