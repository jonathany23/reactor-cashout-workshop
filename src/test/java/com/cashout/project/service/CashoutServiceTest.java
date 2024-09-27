package com.cashout.project.service;

import com.cashout.project.domain.entity.Cashout;
import com.cashout.project.domain.repository.ICashoutRepository;
import com.cashout.project.exeptions.Error400Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CashoutServiceTest {

    @Mock
    ICashoutRepository repository;

    @InjectMocks
    CashoutService cashoutService;

    @Test
    void createCashout() {
        Cashout cashout = new Cashout();
        cashout.setId(1);
        cashout.setAmount(100.0);
        cashout.setUserId(1);

        Mockito.when(repository.save(ArgumentMatchers.any(Cashout.class))).thenReturn(Mono.just(cashout));

        StepVerifier.create(cashoutService.createCashout(cashout))
                .expectNext(cashout)
                .verifyComplete();
    }

    @Test
    void createCashout_sadPath() {
        Cashout cashout = new Cashout();
        cashout.setId(-1);
        cashout.setAmount(100.0);
        cashout.setUserId(1);

        Mockito.when(repository.save(ArgumentMatchers.any(Cashout.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(cashoutService.createCashout(cashout))
                .expectErrorMatches(throwable -> throwable instanceof Error400Exception
                        && throwable.getMessage().equals("Error al guardar el cashout"))
                .verify();
    }

    @Test
    void getCashoutsByUserId() {
        Cashout cashout = new Cashout();
        cashout.setId(1);
        cashout.setAmount(100.0);
        cashout.setUserId(1);

        Cashout cashout2 = new Cashout();
        cashout2.setId(2);
        cashout2.setAmount(200.0);
        cashout2.setUserId(1);

        Mockito.when(repository.findCashoutByUserId(1L)).thenReturn(Flux.just(cashout, cashout2));

        StepVerifier.create(cashoutService.getCashoutsByUserId(1L))
                .expectNext(cashout)
                .expectNext(cashout2)
                .verifyComplete();
    }
}