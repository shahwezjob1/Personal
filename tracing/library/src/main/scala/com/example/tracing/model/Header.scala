package com.example.tracing.model

/**
 *
 * @param key   : The key of the header.
 * @param value : The value of the header. If value is in String then use getBytes() method.
 */
case class Header(key: String, value: Array[Byte])
