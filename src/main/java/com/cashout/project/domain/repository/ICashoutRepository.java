package com.cashout.project.domain.repository;

import com.cashout.project.domain.entity.Cashout;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ICashoutRepository extends ReactiveCrudRepository<Cashout, Long> {
    Flux<Cashout> findCashoutByUserId(Long userId);
}
