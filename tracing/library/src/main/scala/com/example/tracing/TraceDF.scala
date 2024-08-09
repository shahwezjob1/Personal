package com.example.tracing

import com.example.tracing.model.Header
import com.example.tracing.util.DefaultTracingUtil
import com.example.tracing.util.`trait`.TracingUtilTrait
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, lit, udf}
import org.slf4j.{Logger, LoggerFactory}

object TraceDF extends Serializable {

    private val log: Logger = LoggerFactory.getLogger(this.getClass.getName)

    /**
     *
     * @param dataFrame : The dataframe which needs to be processed.
     */
    implicit class DFWithTrace(dataFrame: DataFrame) extends Serializable {

        /**
         * This function allows to generate Traces for each Row.
         *
         * @param traceConfig : Object containing configuration details for Trace and Span.
         * @return : Updated DataFrame.
         */
        def generateTraceForeachRow(traceConfig: TraceConfig, TracingUtil: TracingUtilTrait = DefaultTracingUtil): DataFrame = {
            if (dataFrame.columns.contains("headers")) {
                dataFrame.withColumn("headers", generateTrace(traceConfig, TracingUtil)(col("headers")))
            } else {
                log.warn("Generating Traces for Streaming Dataframe. " +
                        "Dataframe does not have headers column. Appending headers column with null value.")
                dataFrame.withColumn("headers", generateTrace(traceConfig, TracingUtil)(lit(null)))
            }
        }

        /**
         * This function returns a UDF. The UDF generates Span.
         *
         * @param traceConfig : Object containing configuration details for Trace and Span.
         * @return : UDF
         */
        private def generateTrace(traceConfig: TraceConfig, TracingUtil: TracingUtilTrait): UserDefinedFunction = {
            udf((headers: Seq[Header]) => TracingUtil.generateTrace(headers, traceConfig))
        }

    }

}
