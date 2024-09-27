package com.cashout.project;

import com.cashout.project.domain.entity.Cashout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class CashoutControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createCashout(){
        Cashout cashout = new Cashout();
        cashout.setId(100);
        cashout.setUserId(1);
        cashout.setAmount(5.0);

        webTestClient
                .post()
                .uri("/cashout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cashout)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cashout.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void createCashout_sadPath(){
        Cashout cashout = new Cashout();
        cashout.setId(100);
        cashout.setUserId(100);
        cashout.setAmount(500.0);

        webTestClient
                .post()
                .uri("/cashout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cashout)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Usuario no encontrado");
    }

    @Test
    void createCashout_sadPath2(){
        Cashout cashout = new Cashout();
        cashout.setId(100);
        cashout.setUserId(1);
        cashout.setAmount(5000.0);

        webTestClient
                .post()
                .uri("/cashout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cashout)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Saldo insuficiente");
    }

    @Test
    void createCashout_sadPath3(){
        Cashout cashout = new Cashout();
        cashout.setId(100);
        cashout.setUserId(1);
        cashout.setAmount(3.0);

        webTestClient
                .post()
                .uri("/cashout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cashout)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Payment failed");
    }

    @Test
    void getCashoutByUserId(){
        webTestClient
                .get()
                .uri("/cashout/user/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].userId").isEqualTo(1);
    }
}
