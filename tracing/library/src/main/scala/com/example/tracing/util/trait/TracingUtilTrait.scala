package com.example.tracing.util.`trait`

import com.example.tracing.TraceConfig
import com.example.tracing.model.{Header, Traceparent}
import io.opentelemetry.api.trace.{Span, SpanBuilder, SpanContext, TraceFlags, TraceState, TracerProvider}
import io.opentelemetry.context.{Context, Scope}

/**
 * This trait contains the common functions needed to generate traces.
 */
trait TracingUtilTrait extends Serializable {

    /**
     * This method extracts the trace id and span id from headers.
     *
     * @param headers : The headers.
     * @return : Object containing trace id and span id.
     */
    def extractTraceparent(headers: Seq[Header]): Traceparent

    /**
     * This method generates a new span builder object.
     *
     * @param traceConfig : Object containing configuration details for Span Builder object.
     * @return : Object of Span Builder.
     */
    def createSpanBuilder(traceConfig: TraceConfig, tracerProvider: TracerProvider): SpanBuilder

    /**
     * This method adds the parent span to the span builder. If Trace is invalid then it configures the span builder as root span.
     *
     * @param spanBuilder : Object of span builder.
     * @param traceparent   : Object containing trace id and span id of parent span.
     */
    def addTraceparent(spanBuilder: SpanBuilder, traceparent: Traceparent,
                       spanContextCreator : (String, String, TraceFlags, TraceState) => SpanContext,
                       currentContextCreator: SpanContext => Context): Unit

    /**
     * This method starts the span and makes the scope.
     *
     * @param spanBuilder : This function start the Span on the provided Span Builder.
     * @return : Tuple 2 where _1 is the Span object and _2 is the scope object.
     */
    def startTrace(spanBuilder: SpanBuilder): (Span, Scope)

    /**
     * This method updates the value of traceId and spanId header.
     *
     * @param traceparent : The trace id and span id that need to be added to Headers.
     * @param headers   : The Headers that need to be updated.
     * @return : The updated Headers.
     */
    def updateHeaders(traceparent: Traceparent, headers: Seq[Header]): Seq[Header]

    /**
     * This method ends the span and closes the scope.
     *
     * @param span  : The Span which needs to be ended.
     * @param scope : The Scope which needs to be closed.
     */
    def endTrace(span: Span, scope: Scope): Unit

    /**
     *
     * @param headers     : Existing headers.
     * @param traceConfig : Object containing configuration details for Trace and Span.
     * @return : Updated Headers with the updated details of Trace and Span.
     */
    def generateTrace(headers: Seq[Header], traceConfig: TraceConfig): Seq[Header]
}

