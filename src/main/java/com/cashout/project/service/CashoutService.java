package com.cashout.project.service;

import com.cashout.project.domain.entity.Cashout;
import com.cashout.project.domain.repository.ICashoutRepository;
import com.cashout.project.exeptions.Error400Exception;
import com.cashout.project.service.gateway.ICashoutService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CashoutService implements ICashoutService {
    private final ICashoutRepository repository;

    public CashoutService(ICashoutRepository repository){
        this.repository = repository;
    }

    @Override
    public Mono<Cashout> createCashout(Cashout cashout) {
        return repository.save(cashout)
                .onErrorMap(throwable -> new Error400Exception("Error al guardar el cashout"));
    }

    @Override
    public Flux<Cashout> getCashoutsByUserId(Long userId) {
        return repository.findCashoutByUserId(userId);
    }
}
