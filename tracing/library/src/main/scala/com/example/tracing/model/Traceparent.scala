package com.example.tracing.model

case class Traceparent(version: String = "00", traceId: String, spanId: String, traceFlagsHex: String = "01") {
    override def toString: String = s"$version-$traceId-$spanId-$traceFlagsHex"
}