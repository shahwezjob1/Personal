package com.example.tracing

import com.example.tracing.model.Header

case class Person(name: String, age: Int)

case class PersonRow(data: Person)

case class PersonRowWithHeaders(data: Person, headers: Seq[Header])
