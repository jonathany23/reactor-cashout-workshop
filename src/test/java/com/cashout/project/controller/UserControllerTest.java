package com.cashout.project.controller;

import com.cashout.project.domain.entity.User;
import com.cashout.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {UserController.class, GlobalHandleError.class})
@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    UserService userService;

    @Test
    void createUser() {
        var userInput = new User();
        userInput.setId(1);
        userInput.setName("Test");
        userInput.setBalance(100.0);

        Mockito.when(userService.createUser(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(userInput));

        webTestClient
                .post()
                .uri("/user")
                .bodyValue(userInput)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Test")
                .jsonPath("$.balance").isEqualTo(100.0);
    }

    @Test
    void createUser_sadPath() {
        var userInput = new User();
        userInput.setId(-1);
        userInput.setName("Test");
        userInput.setBalance(100.0);

        webTestClient
                .post()
                .uri("/user")
                .bodyValue(userInput)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The id is required");
    }

    @Test
    void createUser_sadPath1() {
        var userInput = new User();
        userInput.setId(1);
        userInput.setName("");
        userInput.setBalance(100.0);

        webTestClient
                .post()
                .uri("/user")
                .bodyValue(userInput)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The name is required");
    }

    @Test
    void createUser_sadPath2() {
        var userInput = new User();
        userInput.setId(1);
        userInput.setName("Test");
        userInput.setBalance(-100.0);

        webTestClient
                .post()
                .uri("/user")
                .bodyValue(userInput)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("The balance must be positive");
    }

    @Test
    void getAllUsers() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(userService.getAllUsers()).thenReturn(Flux.just(user));

        webTestClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Test")
                .jsonPath("$[0].balance").isEqualTo(100.0);
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.just(user));

        webTestClient
                .get()
                .uri("/user/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Test")
                .jsonPath("$.balance").isEqualTo(100.0);
    }

    @Test
    void updateBalance() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(userService.updateBalance(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(user));

        webTestClient
                .put()
                .uri("/user/{id}/balance", 1L)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Test")
                .jsonPath("$.balance").isEqualTo(100.0);
    }
}