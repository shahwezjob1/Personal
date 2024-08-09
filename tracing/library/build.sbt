ThisBuild / scalaVersion := "2.12.15"

val sparkVersion = "3.3.0"
val otVersion = "1.31.0"

lazy val libraryProject = (project in file("."))
        .settings(
            organization := "com.example.tracing",
            name := "library",
            crossPaths := true,
            publishMavenStyle := true,
            version := "0.0.1"
        )
fork := true
coverageEnabled := true

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % Provided
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % Provided
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % Test

libraryDependencies += "io.opentelemetry" % "opentelemetry-sdk-extension-autoconfigure" % otVersion
libraryDependencies += "io.opentelemetry" % "opentelemetry-exporter-jaeger" % otVersion
libraryDependencies += "io.opentelemetry" % "opentelemetry-exporter-otlp" % otVersion
