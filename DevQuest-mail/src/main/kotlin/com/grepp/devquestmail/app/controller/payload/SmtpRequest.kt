package com.grepp.devquestmail.app.controller.payload

data class SmtpRequest(
    val from:String,
    val subject:String,
    val to:List<String>, // 여러명에게 보낼 있도록 (전체 공지 등 일괄 전송을 위함)
    val properties:MutableMap<String, String> = mutableMapOf(),
    val eventType:String,
) {
}