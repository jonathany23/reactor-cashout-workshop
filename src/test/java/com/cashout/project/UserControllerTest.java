package com.cashout.project;

import com.cashout.project.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {


    @Autowired
    private WebTestClient webClient;


    @Test
    void createUser() {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setBalance(100.0);

        webClient
                .post()
                .uri("/user")
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void createUser_sadPath() {
        User user = new User();
        user.setId(-1);
        user.setName("test");
        user.setBalance(500.0);

        webClient
                .post()
                .uri("/user")
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The id is required");
    }

    @Test
    void createUser_sadPath1() {
        User user = new User();
        user.setId(1);
        user.setName("");
        user.setBalance(500.0);

        webClient
                .post()
                .uri("/user")
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The name is required");
    }

    @Test
    void createUser_sadPath2() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(-100.0);

        webClient
                .post()
                .uri("/user")
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The balance must be positive");
    }

    @Test
    void getAllUsers() {
        webClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1);
    }

    @Test
    void getUserById() {
        webClient
                .get()
                .uri("/user/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getUserById_sadPath() {
        webClient
                .get()
                .uri("/user/-1")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Usuario no encontrado");
    }

    @Test
    void updateBalance() {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setBalance(500.0);

        webClient
                .put()
                .uri("/user/1/balance")
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(500.0);
    }
}
