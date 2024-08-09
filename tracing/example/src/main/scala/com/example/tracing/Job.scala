package com.example.tracing

import com.example.tracing.TraceDF._
import io.opentelemetry.api.trace.SpanKind
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.{Encoders, SparkSession}

object Job {

    private val spark = SparkSession.builder()
            .master("local[*]")
            .appName("Job")
            .getOrCreate()

    private val personRowSchema = Encoders.product[PersonRow].schema
    private val personRowWithHeadersSchema = Encoders.product[PersonRowWithHeaders].schema

    private val sourceTraceConfig = new TraceConfigBuilder("source")
            .withSpanKind(SpanKind.CONSUMER)
            .build()

    private val destinationTraceConfig = new TraceConfigBuilder("destination")
            .withSpanKind(SpanKind.PRODUCER)
            .build()

    def main(args: Array[String]): Unit = {

        spark.readStream
                .schema(personRowSchema)
                .json("example\\src\\main\\resources\\source")
                .generateTraceForeachRow(sourceTraceConfig)
                // generate trace and then perform tasks for each row
                .writeStream
                .format("json")
                .outputMode(OutputMode.Append())
                .option("checkpointLocation", "checkpoint/source")
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start("tracing\\example\\src\\main\\resources\\intermediate")

        spark.readStream
                .schema(personRowWithHeadersSchema)
                .json("tracing\\example\\src\\main\\resources\\intermediate")
                //generate trace after completing final task for each row
                .generateTraceForeachRow(destinationTraceConfig)
                .writeStream
                .format("json")
                .outputMode(OutputMode.Append())
                .option("checkpointLocation", "checkpoint/destination")
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start("tracing\\example\\src\\main\\resources\\destination")

        spark.streams.awaitAnyTermination()
    }
}
