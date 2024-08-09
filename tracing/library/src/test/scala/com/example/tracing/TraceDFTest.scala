package com.example.tracing

import com.example.tracing.util.`trait`.TracingUtilTrait
import org.apache.spark.sql.{Column, DataFrame}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{mock, when}
import org.scalatest.funsuite.AnyFunSuite

class TraceDFTest extends AnyFunSuite {

    test("when headers column not exist") {
        val mockDF1 = mock(classOf[DataFrame])
        val mockDF2 = mock(classOf[DataFrame])
        val mockTraceConfig = mock(classOf[TraceConfig])
        val mockTracingUtil = mock(classOf[TracingUtilTrait])
        when(mockDF1.columns).thenReturn(Array.apply("col1", "col2"))
        when(mockDF1.withColumn(ArgumentMatchers.eq("headers"), any(classOf[Column]))).thenReturn(mockDF2)
        import TraceDF._
        val resDF = mockDF1.generateTraceForeachRow(mockTraceConfig, mockTracingUtil)
        assert(resDF === mockDF2)
    }

    test("when headers column exist") {
        val mockDF1 = mock(classOf[DataFrame])
        val mockDF2 = mock(classOf[DataFrame])
        val mockTraceConfig = mock(classOf[TraceConfig])
        val mockTracingUtil = mock(classOf[TracingUtilTrait])
        when(mockDF1.columns).thenReturn(Array.apply("headers", "col2"))
        when(mockDF1.withColumn(ArgumentMatchers.eq("headers"), any(classOf[Column]))).thenReturn(mockDF2)
        import TraceDF._
        val resDF = mockDF1.generateTraceForeachRow(mockTraceConfig, mockTracingUtil)
        assert(resDF === mockDF2)
    }

}
