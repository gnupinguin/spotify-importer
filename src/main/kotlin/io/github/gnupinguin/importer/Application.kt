package io.github.gnupinguin.importer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository


@EnableFeignClients
@SpringBootApplication
class Application

fun main(args: Array<String>) {
        runApplication<Application>(*args)
}

@EnableWebSecurity
class OAuth2LoginSecurityConfig: WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
                http.authorizeRequests { a ->
                        a.antMatchers("/", "/error").permitAll()
                                .anyRequest().authenticated()
                }
//                        .exceptionHandling { e ->
//                        e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                }
                .csrf{c ->
                        c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                }
                        .oauth2Login()

        }
}

