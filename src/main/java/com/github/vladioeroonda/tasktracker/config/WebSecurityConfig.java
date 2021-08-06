package com.github.vladioeroonda.tasktracker.config;

import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userDetailsService;

    public WebSecurityConfig(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/tracker/project/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.GET, "/api/tracker/release/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.GET, "/api/tracker/task/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
//                .antMatchers(HttpMethod.GET, "/api/tracker/user/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/api/tracker/task/management/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/api/tracker/task/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api/tracker/project/management/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api/tracker/project/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api/tracker/release/management/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api/tracker/release/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api/tracker/user/**").hasAuthority(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
