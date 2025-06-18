package com.grepp.devquestmail.app.model.code

enum class MailTemplatePath(val path:String) {
    SIGNUP_VERIFY("/mail/signup-verification"),
    SIGNUP_COMPLETE("/mail/signup-complete")
}