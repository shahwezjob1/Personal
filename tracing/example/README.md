# TraceDF Example Project

This example project demonstrates the usage of the `TraceDF` library for generating tracing spans in a Spark Streaming job. 
The project reads data from a source directory and generates trace spans for each row. 
Next it performs intermediate processing. 
Then it writes the results to a destination directory, generating additional traces.
This is the [readme of TraceDF library](./../library/README.md).

## Prerequisites

- **Setup Hadoop**: Setup Hadoop Home and add Hadoop bin to path. Hadoop 3.0.0 `winutils.exe` and `hadoop.dll` can be used for quick setup.
- **Trace Collector**: Set up a Trace Collector. Jaeger all in one can be used.
- **Scala Plugin**: Add Scala Plugin to IntelliJ.

## Usage

Step 1 : Setup TraceDF library. Below commands are for IntelliJ sbt shell.
```
project libraryProject
publishLocal
```

Step 2 : Update run configuration of [Job.scala](src/main/scala/com/example/tracing/Job.scala). For additional configuration look [here](https://opentelemetry.io/docs/languages/sdk-configuration/general/).
```
-Dotel.java.global-autoconfigure.enabled=true -Dotel.service.name=SparkService
```

Step 3 : Run `Job.scala`. 
Once the application has started add files to `resources/source` directory. The file will be read and written to `resources/destination` directory. In the process traces will be generated which can be seen in trace collector. Sample files are present in `src/temp` directory.

