package com.cashout.project.controller;

import com.cashout.project.controller.dto.PaymentDto;
import com.cashout.project.domain.entity.Cashout;
import com.cashout.project.domain.entity.User;
import com.cashout.project.exeptions.Error400Exception;
import com.cashout.project.service.CashoutService;
import com.cashout.project.service.UserService;
import com.cashout.project.service.gateway.IRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {CashoutController.class, GlobalHandleError.class})
@WebFluxTest(CashoutController.class)
class CashoutControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CashoutService cashoutService;

    @MockBean
    UserService userService;

    @MockBean
    IRestClient restClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createCashout() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Cashout cashout = new Cashout();
        cashout.setUserId(1);
        cashout.setAmount(50.0);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentStatus("approved");


        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.just(user));
        Mockito.when(userService.updateBalance(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(user));
        Mockito.when(cashoutService.createCashout(ArgumentMatchers.any(Cashout.class))).thenReturn(Mono.just(cashout));
        Mockito.when(restClient.payment(1, 50.0)).thenReturn(Mono.just(paymentDto));

        webTestClient
                .post()
                .uri("/cashout")
                .bodyValue(cashout)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo(1)
                .jsonPath("$.amount").isEqualTo(50.0);
    }

    @Test
    void createCashout_sadPath() {
        Cashout cashout = new Cashout();
        cashout.setUserId(1);
        cashout.setAmount(150.0);

        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.empty());

        webTestClient
                .post()
                .uri("/cashout")
                .bodyValue(cashout)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Usuario no encontrado");
    }

    @Test
    void createCashout_sadPath2() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Cashout cashout = new Cashout();
        cashout.setUserId(1);
        cashout.setAmount(150.0);

        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.just(user));

        webTestClient
                .post()
                .uri("/cashout")
                .bodyValue(cashout)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Saldo insuficiente");
    }

    @Test
    void createCashout_sadPath3() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Cashout cashout = new Cashout();
        cashout.setUserId(1);
        cashout.setAmount(50.0);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentStatus("failed");

        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.just(user));
        Mockito.when(restClient.payment(1, 50.0)).thenReturn(Mono.just(paymentDto));

        webTestClient
                .post()
                .uri("/cashout")
                .bodyValue(cashout)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Payment failed");
    }

    @Test
    void getCashoutByUserId() {
        Cashout cashout = new Cashout();
        cashout.setId(1);
        cashout.setUserId(1);
        cashout.setAmount(50.0);

        Mockito.when(cashoutService.getCashoutsByUserId(1L)).thenReturn(Flux.just(cashout));

        webTestClient
                .get()
                .uri("/cashout/user/{userId}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].userId").isEqualTo(1)
                .jsonPath("$[0].amount").isEqualTo(50.0);
    }
}