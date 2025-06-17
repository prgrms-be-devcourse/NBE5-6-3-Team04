package com.grepp.devquestmail

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DevQuestMailApplication

fun main(args: Array<String>) {

    // .env 파일 로드
    val dotenv = Dotenv.configure()
        .ignoreIfMissing() // .env 파일이 없어도 예외 발생하지 않도록
        .load()

    System.setProperty("MAIL_USERNAME", dotenv["MAIL_USERNAME"])
    System.setProperty("MAIL_PASSWORD", dotenv["MAIL_PASSWORD"])

    runApplication<DevQuestMailApplication>(*args)
}
