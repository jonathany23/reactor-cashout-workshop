package com.cashout.project.service.gateway;

import com.cashout.project.domain.entity.Cashout;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICashoutService {
    Mono<Cashout> createCashout(Cashout cashout);
    Flux<Cashout> getCashoutsByUserId(Long userId);
}
