ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

coverageEnabled := true

lazy val rootProject = (project in file("."))
        .settings(
            name := "tracing"
        )
        .aggregate(libraryProject)
        .aggregate(exampleProject)

lazy val libraryProject = (project in file("library"))
        .settings(
            name := "library"
        )

lazy val exampleProject = (project in file("example"))
        .settings(
            name := "example"
        )

