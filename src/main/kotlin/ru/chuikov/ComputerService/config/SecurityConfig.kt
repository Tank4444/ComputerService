package ru.chuikov.ComputerService.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.chuikov.ComputerService.dao.Role


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val accService:UserDetailsService,
    private val passwordEncoder: PasswordEncoder
):WebSecurityConfigurerAdapter() {

    @Value("\${user.oauth.user.username}")
    private val username: String? = null

    @Value("\${user.oauth.user.password}")
    private val password: String? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .antMatcher("/api/**")
            .authorizeRequests()
            .antMatchers("/oauth/authorize**", "/login**", "/error**","/**","/h2**","/h2/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy((SessionCreationPolicy.STATELESS))
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


    override fun configure(web: WebSecurity) {
        web.ignoring()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(accService).passwordEncoder(passwordEncoder)
            .and().inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("password")).roles(Role.ROLE_ADMIN.name)
    }

}