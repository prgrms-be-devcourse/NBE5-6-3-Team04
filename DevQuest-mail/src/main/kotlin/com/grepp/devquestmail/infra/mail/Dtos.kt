package com.grepp.devquestmail.infra.mail

data class SmtpDto(
    val from:String,
    val subject:String,
    val to:String,
    val properties:Map<String, Any>? = mutableMapOf(),
    val templatePath:String,
)