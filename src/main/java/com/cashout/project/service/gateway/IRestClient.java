package com.cashout.project.service.gateway;

import com.cashout.project.controller.dto.PaymentDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IRestClient {
    @PostExchange("/payment/{userId}/{amount}")
    Mono<PaymentDto> payment(@PathVariable("userId") long userId, @PathVariable("amount") double amount);
}
