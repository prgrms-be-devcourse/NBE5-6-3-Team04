package com.grepp.devquestmail.infra.mail

import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class MailTemplateTest {

    @Autowired
    lateinit var template: MailTemplate

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            val dotenv = Dotenv.configure().ignoreIfMissing().load()
            System.setProperty("MAIL_USERNAME", dotenv["MAIL_USERNAME"])
            System.setProperty("MAIL_PASSWORD", dotenv["MAIL_PASSWORD"])
        }
    }

    @Test
    fun testSend(): Unit = runBlocking {
        val dto = SmtpDto(
            templatePath = "/mail/signup-verification",
            from = "DevQuest",
            to = "dev5lsh038@gmail.com",
            subject = "mail test"
        )

        println("USERNAME=${System.getProperty("MAIL_USERNAME")}")
        println("PASSWORD=${System.getProperty("MAIL_PASSWORD")}")

        val semaphore = Semaphore(1)
        launch(Dispatchers.IO) {
            semaphore.withPermit {
                template.send(dto)
            }
        }
    }
}
