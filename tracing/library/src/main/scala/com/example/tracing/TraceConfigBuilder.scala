package com.example.tracing

import com.example.tracing.util.DefaultTracingUtil
import com.example.tracing.util.`trait`.TracingUtilTrait
import io.opentelemetry.api.trace.SpanKind

/**
 * This Builder Class helps to create object of [[TraceConfig]]
 *
 * @param spanName : Name of the Span.
 */
class TraceConfigBuilder(spanName: String) {

    private var attributes: Map[String, String] = Map.empty
    private var spanKind: SpanKind = SpanKind.INTERNAL
    private var instrumentationScopeName: String = "Custom-Instrumentation"
    private var instrumentationScopeVersion: String = "0.0.1"
    private var tracingUtil: TracingUtilTrait = DefaultTracingUtil

    /**
     * This method updates the value of spanKind.
     *
     * @param spanKind : The new SpanKind value.
     * @return : The updated TraceConfigBuilder object.
     */
    def withSpanKind(spanKind: SpanKind): TraceConfigBuilder = {
        this.spanKind = spanKind
        this
    }

    /**
     * This method updates the value of instrumentationScopeName.
     *
     * @param name : The new instrumentation scope name.
     * @return : The updated TraceConfigBuilder object.
     */
    def withInstrumentationScopeName(name: String): TraceConfigBuilder = {
        this.instrumentationScopeName = name
        this
    }

    /**
     * This method updates the value of instrumentationScopeVersion.
     *
     * @param version : The new instrumentation scope version.
     * @return : The updated TraceConfigBuilder object.
     */
    def withInstrumentationScopeVersion(version: String): TraceConfigBuilder = {
        this.instrumentationScopeVersion = version
        this
    }

    /**
     * This method helps in adding custom Tag which will be added to Span.
     *
     * @param key   : key of the Tag.
     * @param value : Value of the Tag.
     * @return : This instance of CustomSpanBuilder after configuring it with provided parameters.
     */
    def withAttribute(key: String, value: String): TraceConfigBuilder = {
        this.attributes += (key -> value)
        this
    }

    /**
     * This method updates the value of tracingUtil.
     *
     * @param tracingUtil : The new TracingUtilTrait value.
     * @return : The updated TraceConfigBuilder object.
     */
    def withTracingUtil(tracingUtil: TracingUtilTrait): TraceConfigBuilder = {
        this.tracingUtil = tracingUtil
        this
    }

    /**
     * This method builds the object of [[TraceConfig]].
     *
     * @return : Object of [[TraceConfig]].
     */
    def build(): TraceConfig = {
        TraceConfig(spanName, spanKind, instrumentationScopeName, instrumentationScopeVersion, attributes)
    }

}
