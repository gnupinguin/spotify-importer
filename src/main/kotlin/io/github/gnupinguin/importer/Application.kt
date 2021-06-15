package io.github.gnupinguin.importer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Profile

@EnableFeignClients
@SpringBootApplication
class Application

fun main(args: Array<String>) {
        runApplication<Application>(*args)
}

