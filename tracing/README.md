# TraceDF Project

This repository contains two main modules:

1. **TraceDF Library**: A Scala library designed to generate W3C traces for each row of a Spark DataFrame.
2. **Example Project**: A sample project demonstrating the usage of the `TraceDF` library in a Spark Streaming job.

## TraceDF Library

The `TraceDF` library adds W3C traces to each row of dataframe.

### Key Features

- Row-level tracing for Spark DataFrames.
- Configurable trace and span attributes.
- Integration with existing tracing frameworks like OpenTelemetry.

For more details, refer to the [TraceDF Library README](./library/README.md).

## Example Project

The example project demonstrates how to integrate the `TraceDF` library into a Spark Streaming job. It reads JSON data
from a source directory, generates tracing spans, and writes the processed data to an output directory, all while
maintaining traceability throughout the data pipeline.

### Key Features

- Demonstrates usage of the `TraceDF` library.
- Includes setup instructions for running the Spark job locally.

For more details, refer to the [Example Project README](./example/README.md).

## Repository Structure

```plaintext
├── library/            # The TraceDF library module
│   ├── src/            # Source code for the TraceDF library
│   └── README.md       # Documentation for the TraceDF library
├── example/            # The Example Project module
│   ├── src/            # Source code for the Example Project
│   └── README.md       # Documentation for the Example Project
└── README.md           # Parent directory README
```