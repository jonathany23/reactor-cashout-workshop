package com.cashout.project.controller;

import com.cashout.project.controller.dto.PaymentDto;
import com.cashout.project.domain.entity.Cashout;
import com.cashout.project.domain.entity.User;
import com.cashout.project.exeptions.Error400Exception;
import com.cashout.project.exeptions.UserNotFound;
import com.cashout.project.service.CashoutService;
import com.cashout.project.service.UserService;
import com.cashout.project.service.gateway.IRestClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@RequestMapping("/cashout")
public class CashoutController {

    private final UserService userService;
    private final IRestClient restClient;
    private final CashoutService cashoutService;

    public CashoutController(UserService userService, IRestClient restClient, CashoutService cashoutService) {
        this.userService = userService;
        this.restClient = restClient;
        this.cashoutService = cashoutService;
    }

    @PostMapping
    public Mono<Cashout> createCashout(@Valid @RequestBody Cashout cashout) {
        return userService.getUserById(cashout.getUserId())
                .switchIfEmpty(Mono.error(new UserNotFound("Usuario no encontrado")))
                .flatMap(user -> validateBalance(user, cashout))
                .flatMap(user -> processPayment(user, cashout));
    }

    private Mono<User> validateBalance(User user, Cashout cashout) {
        return user.getBalance() >= cashout.getAmount()
                ? Mono.just(user)
                : Mono.error(new Error400Exception("Saldo insuficiente"));
    }

    private Mono<Cashout> processPayment(User user, Cashout cashout) {
        return getPayment(cashout)
                .flatMap(payment -> payment.getPaymentStatus().equals("approved")
                        ? updateUserBalance(user, cashout).then(cashoutService.createCashout(cashout))
                        : Mono.error(new Error400Exception("Payment failed")));
    }

    private Mono<Void> updateUserBalance(User user, Cashout cashout) {
        user.setBalance(user.getBalance() - cashout.getAmount());
        return userService.updateBalance(user)
                .then();
    }

    private Mono<PaymentDto> getPayment(Cashout cashout) {
        return restClient.payment(cashout.getUserId(), cashout.getAmount())
                .timeout(Duration.ofSeconds(3))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(error -> !(error instanceof Error400Exception)))
                .doOnError(System.out::println);
    }

    @GetMapping("/user/{userId}")
    public Flux<Cashout> getCashoutByUserId(@PathVariable Long userId) {
        return cashoutService.getCashoutsByUserId(userId);
    }
}
