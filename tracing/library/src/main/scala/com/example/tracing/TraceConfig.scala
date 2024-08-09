package com.example.tracing

import io.opentelemetry.api.trace.SpanKind

/**
 * This object provides configuration
 *
 * @param spanName   Name of the span
 * @param spanKind   Type of the span
 * @param attributes Additional constant key value pairs added as tags to span
 */

case class TraceConfig(spanName: String, spanKind: SpanKind, instrumentationScopeName: String,
                       instrumentationScopeVersion: String, attributes: Map[String, String])
