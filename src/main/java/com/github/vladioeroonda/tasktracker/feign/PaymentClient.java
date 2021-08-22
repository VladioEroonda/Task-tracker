package com.github.vladioeroonda.tasktracker.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "payment-service",
        url = "${payment-service.api-url}")
public interface PaymentClient {

    @GetMapping("/api/payment/v1/check")
    Boolean getPaymentCheckResult(
            @RequestParam String customerAccountId,
            @RequestParam String developerAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam String comment
    );
}
