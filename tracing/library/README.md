# TraceDF Library

The `TraceDF` library is designed to generated W3C traces for each row of Dataframe. This library internally uses below standard opentelemetry libraries.
```
val otVersion = "1.31.0"
libraryDependencies += "io.opentelemetry" % "opentelemetry-sdk-extension-autoconfigure" % otVersion
libraryDependencies += "io.opentelemetry" % "opentelemetry-exporter-jaeger" % otVersion
libraryDependencies += "io.opentelemetry" % "opentelemetry-exporter-otlp" % otVersion
```

## Features

- **Row-Level Tracing**: Automatically generates and attaches W3C traces to each row in DataFrame.
- **Flexible Configuration**: Provides a configurable builder for creating customizing the traces.
- **Integrates with Existing Traces**: If an existing W3C trace is found in the `headers` column, it is used as a parent trace; otherwise, a new trace is generated.
- **Customizable Tracing Utility**: Allows to inject custom implementations of the tracing utility. Hence, the logic to generate W3C traces can be updated.

## Getting Started

### Installation

Add the library to your project's dependencies. If using SBT:

```
libraryDependencies += "com.example.tracing" %% "library" % "0.0.1"
```

### Usage
Here's how to use the TraceDF library in your project:

Step 1: Setup Run Configuration
Add below CLI commands to the java options. For more details on what configurations can be used visit [this link](https://opentelemetry.io/docs/languages/sdk-configuration/general/).
```
-Dotel.java.global-autoconfigure.enabled=true -Dotel.service.name=SparkService
```

Step 2: Create a TraceConfig Object
Use the TraceConfigBuilder to configure and create a TraceConfig object.

```scala
import io.opentelemetry.api.trace.SpanKind
import com.example.tracing.TraceConfigBuilder

val traceConfig = new TraceConfigBuilder("spanName")
        .withSpanKind(SpanKind.SERVER)
        .withInstrumentationScopeName("CustomScope")
        .withAttribute("key1", "value1")
        .build()
```
Step 3: Generate Traces for Each Row

```scala
val spark: SparkSession = _ // create or get Spark session
val df: DataFrame = _ // load or create your DataFrame

import com.example.tracing.TraceDF.DFWithTrace

val tracedDF = df.generateTraceForeachRow(traceConfig)
```

