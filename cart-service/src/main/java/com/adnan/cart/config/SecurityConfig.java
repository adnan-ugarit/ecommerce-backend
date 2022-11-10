package com.adnan.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable().httpBasic().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/cart").permitAll()
                .mvcMatchers(HttpMethod.POST, "/cart/add/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/cart/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/cart/clear").permitAll()
                .mvcMatchers(HttpMethod.GET, "/cart/fetch").permitAll()
                .anyRequest().authenticated();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                                   "/configuration/ui",
                                   "/swagger-resources/**",
                                   "/configuration/security",
                                   "/swagger-ui.html",
                                   "/webjars/**")
                .antMatchers("/actuator/**");
    }
    
}
