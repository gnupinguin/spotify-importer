package io.github.gnupinguin.spotify

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository


@EnableCaching
@EnableFeignClients
@ConfigurationPropertiesScan
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
                }.csrf { c ->
                        c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                }.logout { l ->
                        l.logoutSuccessUrl("/").permitAll()
                }.oauth2Login()

        }
}

