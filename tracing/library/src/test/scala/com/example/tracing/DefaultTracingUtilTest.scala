package com.example.tracing

import com.example.tracing.model.{Header, Traceparent}
import com.example.tracing.util.DefaultTracingUtil
import io.opentelemetry.api.trace._
import io.opentelemetry.context.{Context, Scope}
import org.mockito.Mockito.{verify, when}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar.mock

class DefaultTracingUtilTest extends AnyFunSuite{

    test("extractTraceparent should return null if headers are null") {
        val result = DefaultTracingUtil.extractTraceparent(null)
        assert(result == null)
    }

    test("extractTraceparent should return null if headers are empty") {
        val headers = Seq.empty[Header]
        val result = DefaultTracingUtil.extractTraceparent(headers)
        assert(result == null)
    }

    test("extractTraceparent should return null if traceparent header is missing") {
        val headers = Seq(Header("other", "value".getBytes))
        val result = DefaultTracingUtil.extractTraceparent(headers)
        assert(result == null)
    }

    test("extractTraceparent should return null if traceparent header value is not properly formatted") {
        val headers = Seq(Header("traceparent", "invalid-traceparent".getBytes))
        val result = DefaultTracingUtil.extractTraceparent(headers)
        assert(result == null)
    }

    test("extractTraceparent should return valid Traceparent object if traceparent header value is properly formatted") {
        val traceparentValue = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01"
        val headers = Seq(Header("traceparent", traceparentValue.getBytes))
        val result = DefaultTracingUtil.extractTraceparent(headers)
        assert(result != null)
        assert(result.version == "00")
        assert(result.traceId == "4bf92f3577b34da6a3ce929d0e0e4736")
        assert(result.spanId == "00f067aa0ba902b7")
        assert(result.traceFlagsHex == "01")
    }

    test("extractTraceparent should return null if traceparent header value has incorrect number of parts") {
        val traceparentValue = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7"
        val headers = Seq(Header("traceparent", traceparentValue.getBytes))
        val result = DefaultTracingUtil.extractTraceparent(headers)
        assert(result == null)
    }

    test("createSpanBuilder should work correctly") {
        val traceConfig = TraceConfig(
            spanName = "test-span",
            spanKind = SpanKind.INTERNAL,
            attributes = Map("attr1" -> "value1", "attr2" -> "value2"),
            instrumentationScopeName = "Custom-Instrumentation",
            instrumentationScopeVersion = "0.0.1"
        )

        val mockTracerProvider = mock[io.opentelemetry.api.trace.TracerProvider]
        val mockTracer = mock[Tracer]
        val mockSpanBuilder = mock[SpanBuilder]

        when(mockTracerProvider.get(traceConfig.instrumentationScopeName, traceConfig.instrumentationScopeVersion)).thenReturn(mockTracer)
        when(mockTracer.spanBuilder(traceConfig.spanName)).thenReturn(mockSpanBuilder)
        when(mockSpanBuilder.setSpanKind(traceConfig.spanKind)).thenReturn(mockSpanBuilder)
        when(mockSpanBuilder.setAttribute("attr1", "value1")).thenReturn(mockSpanBuilder)
        when(mockSpanBuilder.setAttribute("attr2", "value2")).thenReturn(mockSpanBuilder)

        val result = DefaultTracingUtil.createSpanBuilder(traceConfig, mockTracerProvider)

        verify(mockTracerProvider).get(traceConfig.instrumentationScopeName, traceConfig.instrumentationScopeVersion)
        verify(mockTracer).spanBuilder(traceConfig.spanName)
        verify(mockSpanBuilder).setSpanKind(traceConfig.spanKind)
        verify(mockSpanBuilder).setAttribute("attr1", "value1")
        verify(mockSpanBuilder).setAttribute("attr2", "value2")
        assert(result == mockSpanBuilder)
    }

    test("addTraceparent should setNoParent when traceparent is null") {
        val mockSpanBuilder = mock[SpanBuilder]
        DefaultTracingUtil.addTraceparent(mockSpanBuilder, null)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setNoParent when traceparent traceId is null") {
        val mockSpanBuilder = mock[SpanBuilder]
        val traceparent = Traceparent(null, "spanId", "traceFlags", "traceState")
        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setNoParent when traceparent spanId is null") {
        val mockSpanBuilder = mock[SpanBuilder]
        val traceparent = Traceparent("traceId", null, "traceFlags", "traceState")
        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setNoParent when traceparent traceId is empty") {
        val mockSpanBuilder = mock[SpanBuilder]
        val traceparent = Traceparent("", "spanId", "traceFlags", "traceState")
        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setNoParent when traceparent spanId is empty") {
        val mockSpanBuilder = mock[SpanBuilder]
        val traceparent = Traceparent("traceId", "", "traceFlags", "traceState")
        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setNoParent when context is not valid") {
        val mockSpanBuilder = mock[SpanBuilder]
        val traceparent = Traceparent("invalidTraceId", "invalidSpanId", "traceFlags", "traceState")
        val mockSpanContext = mock[SpanContext]

        when(mockSpanContext.isValid).thenReturn(false)
        val mockSpanContextCreator: (String, String, TraceFlags, TraceState) => SpanContext = (_, _, _, _) => mockSpanContext

        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent, mockSpanContextCreator)
        verify(mockSpanBuilder).setNoParent()
    }

    test("addTraceparent should setParent when context is valid") {
        val mockSpanBuilder = mock[SpanBuilder]
        val mockSpanContext = mock[SpanContext]
        val mockCurrentContext = mock[Context]

        val traceparent = Traceparent("validTraceId", "validSpanId", "traceFlags", "traceState")

        val mockSpanContextCreator: (String, String, TraceFlags, TraceState) => SpanContext = (_, _, _, _) => mockSpanContext
        val mockCurrentContextCreator: SpanContext => Context = _ => mockCurrentContext

        when(mockSpanContext.isValid).thenReturn(true)

        DefaultTracingUtil.addTraceparent(mockSpanBuilder, traceparent, mockSpanContextCreator, mockCurrentContextCreator)

        verify(mockSpanBuilder).setParent(mockCurrentContext)
    }

    test("startTrace should start a span and make it current") {
        val mockSpanBuilder = mock[SpanBuilder]
        val mockSpan = mock[Span]
        val mockScope = mock[Scope]
        val mockSpanContext = mock[SpanContext]

        when(mockSpanBuilder.startSpan()).thenReturn(mockSpan)
        when(mockSpan.makeCurrent()).thenReturn(mockScope)
        when(mockSpan.getSpanContext).thenReturn(mockSpanContext)
        when(mockSpanContext.getTraceId).thenReturn("traceId")
        when(mockSpanContext.getSpanId).thenReturn("spanId")
        when(mockSpanContext.getTraceFlags).thenReturn(TraceFlags.getSampled)

        val result = DefaultTracingUtil.startTrace(mockSpanBuilder)

        verify(mockSpanBuilder).startSpan()
        verify(mockSpan).makeCurrent()
        verify(mockSpan).getSpanContext
        verify(mockSpanContext).getTraceId
        verify(mockSpanContext).getSpanId
        verify(mockSpanContext).getTraceFlags

        assert(result == (mockSpan, mockScope))
    }

    test("updateHeaders should add new traceparent header and not remove other existing header") {
        val traceparent = Traceparent("traceId", "spanId", "traceFlags", "traceState")
        val headers = Seq(
            Header("other", "value".getBytes),
            Header("traceparent", "oldTraceparent".getBytes)
        )

        val updatedHeaders = DefaultTracingUtil.updateHeaders(traceparent, headers)

        assert(updatedHeaders.size == 2)
        assert(updatedHeaders.exists(h => h.key == "traceparent" && new String(h.value) == traceparent.toString))
        assert(updatedHeaders.exists(h => h.key == "other" && new String(h.value) == "value"))
    }

    test("updateHeaders should handle null headers gracefully") {
        val traceparent = Traceparent("traceId", "spanId", "traceFlags", "traceState")

        val updatedHeaders = DefaultTracingUtil.updateHeaders(traceparent, null)

        assert(updatedHeaders.size == 1)
        assert(updatedHeaders.head.key == "traceparent")
        assert(new String(updatedHeaders.head.value) == traceparent.toString)
    }

    test("updateHeaders should handle empty headers sequence") {
        val traceparent = Traceparent("traceId", "spanId", "traceFlags", "traceState")

        val updatedHeaders = DefaultTracingUtil.updateHeaders(traceparent, Seq.empty)

        assert(updatedHeaders.size == 1)
        assert(updatedHeaders.head.key == "traceparent")
        assert(new String(updatedHeaders.head.value) == traceparent.toString)
    }

    test("endTrace should close the scope and end the span") {
        val mockSpan = mock[Span]
        val mockScope = mock[Scope]
        val mockSpanContext = mock[SpanContext]
        val mockTraceFlags = mock[TraceFlags]

        when(mockSpan.getSpanContext).thenReturn(mockSpanContext)
        when(mockSpanContext.getTraceId).thenReturn("traceId")
        when(mockSpanContext.getSpanId).thenReturn("spanId")
        when(mockSpanContext.getTraceFlags).thenReturn(mockTraceFlags)
        when(mockTraceFlags.asHex()).thenReturn("traceFlagsHex")

        DefaultTracingUtil.endTrace(mockSpan, mockScope)

        verify(mockScope).close()
        verify(mockSpan).end()
    }

}
