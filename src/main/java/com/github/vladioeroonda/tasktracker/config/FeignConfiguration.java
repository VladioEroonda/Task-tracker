package com.github.vladioeroonda.tasktracker.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.github.vladioeroonda.tasktracker.feign")
public class FeignConfiguration {
}