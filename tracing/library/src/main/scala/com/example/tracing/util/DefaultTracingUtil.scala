package com.example.tracing.util

import com.example.tracing.TraceConfig
import com.example.tracing.model.{Header, Traceparent}
import com.example.tracing.util.`trait`.TracingUtilTrait
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace._
import io.opentelemetry.context.{Context, Scope}
import org.slf4j.{Logger, LoggerFactory}

/**
 * This object contains the common functions needed to generate traces
 */
object DefaultTracingUtil extends TracingUtilTrait {

    private val log: Logger = LoggerFactory.getLogger(this.getClass.getName)

    /**
     * This method extracts the trace id and span id from headers.
     *
     * @param headers : The headers.
     * @return : Object containing trace id and span id.
     */
    def extractTraceparent(headers: Seq[Header]): Traceparent = {
        if (headers == null || headers.isEmpty) {
            null
        } else if (headers.exists(_.key.equals("traceparent"))) {
            val traceparent = new String(headers.filter(_.key.equals("traceparent")).head.value)
            val traceData = traceparent.split("-")
            if (traceData.size != 4) {
                null
            } else {
                Traceparent(traceData(0), traceData(1), traceData(2), traceData(3))
            }
        } else {
            null
        }
    }

    /**
     * This method generates a new span builder object.
     *
     * @param traceConfig : Object containing configuration details for Span Builder object.
     * @return : Object of Span Builder.
     */
    def createSpanBuilder(traceConfig: TraceConfig, tracerProvider: TracerProvider = GlobalOpenTelemetry.get.getTracerProvider): SpanBuilder = {
        val tracer = tracerProvider.get(traceConfig.instrumentationScopeName, traceConfig.instrumentationScopeVersion)
        val spanBuilder = tracer.spanBuilder(traceConfig.spanName).setSpanKind(traceConfig.spanKind)
        traceConfig.attributes.foreach {
            case (key: String, value: String) => spanBuilder.setAttribute(key, value)
        }
        spanBuilder
    }

    /**
     * This method adds the parent span to the span builder. If Trace is invalid then it configures the span builder as root span.
     *
     * @param spanBuilder : Object of span builder.
     * @param traceparent : Object containing trace id and span id of parent span.
     */
    def addTraceparent(spanBuilder: SpanBuilder, traceparent: Traceparent,
                       spanContextCreator: (String, String, TraceFlags, TraceState) => SpanContext = SpanContext.create,
                       currentContextCreator: SpanContext => Context = spanContext => Context.current().`with`(Span.wrap(spanContext))): Unit = {
        if (traceparent == null || traceparent.traceId == null || traceparent.spanId == null ||
                traceparent.traceId.isEmpty || traceparent.spanId.isEmpty) {
            spanBuilder.setNoParent()
        } else {
            val context = spanContextCreator(traceparent.traceId, traceparent.spanId, TraceFlags.getSampled, TraceState.getDefault)
            if (context.isValid) {
                spanBuilder.setParent(currentContextCreator(context))
            } else {
                spanBuilder.setNoParent()
            }
        }
    }

    /**
     * This method starts the span and makes the scope.
     *
     * @param spanBuilder : This function start the Span on the provided Span Builder.
     * @return : Tuple 2 where _1 is the Span object and _2 is the scope object.
     */
    def startTrace(spanBuilder: SpanBuilder): (Span, Scope) = {
        val span = spanBuilder.startSpan()
        val scope = span.makeCurrent()
        val spanContext = span.getSpanContext
        val trace: String = s"00-${spanContext.getTraceId}-${spanContext.getSpanId}-${spanContext.getTraceFlags.asHex()}"
        log.info(s"Started : $trace")
        (span, scope)
    }

    /**
     * This method adds the new value of traceparent in header.
     *
     * @param traceparent : The new traceparent that need to be added to Headers.
     * @param headers     : The Headers that need to be updated.
     * @return : The updated Headers.
     */
    def updateHeaders(traceparent: Traceparent, headers: Seq[Header]): Seq[Header] = {
        val filteredHeaders: Seq[Header] = if (headers != null) {
            headers.filterNot(h => h.key.equals("traceparent"))
        } else {
            Seq.empty[Header]
        }
        filteredHeaders :+ Header("traceparent", traceparent.toString.getBytes)
    }

    /**
     * This method ends the span and closes the scope.
     *
     * @param span  : The Span which needs to be ended.
     * @param scope : The Scope which needs to be closed.
     */
    def endTrace(span: Span, scope: Scope): Unit = {
        scope.close()
        span.end()
        val spanContext = span.getSpanContext
        val trace: String = s"00-${spanContext.getTraceId}-${spanContext.getSpanId}-${spanContext.getTraceFlags.asHex()}"
        log.info(s"Stopped : $trace")
    }

    /**
     *
     * @param headers     : Existing headers.
     * @param traceConfig : Object containing configuration details for Trace and Span.
     * @return : Updated Headers with the updated details of Trace and Span.
     */
    override def generateTrace(headers: Seq[Header], traceConfig: TraceConfig): Seq[Header] = {
        val traceparent: Traceparent = extractTraceparent(headers)
        val spanBuilder: SpanBuilder = createSpanBuilder(traceConfig)
        addTraceparent(spanBuilder, traceparent)
        val (span, scope) = startTrace(spanBuilder)
        val spanContext: SpanContext = span.getSpanContext
        val updatedTraceparent = Traceparent("00", spanContext.getTraceId, spanContext.getSpanId, spanContext.getTraceFlags.asHex())
        val updatedHeaders = updateHeaders(updatedTraceparent, headers)
        endTrace(span, scope)
        updatedHeaders
    }
}
