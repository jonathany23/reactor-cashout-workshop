package com.cashout.project.service;

import com.cashout.project.domain.entity.User;
import com.cashout.project.domain.repository.IUserRepository;
import com.cashout.project.exeptions.UserNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    IUserRepository repository;

    @InjectMocks
    UserService userService;

    @Test
    void createUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(repository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.createUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void createUser_sadPath() {
        User user = new User();
        user.setId(-1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(repository.save(user)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(userService.createUser(user))
                .expectError()
                .verify();
    }

    @Test
    void getAllUsers() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Test2");
        user2.setBalance(200.0);

        Mockito.when(repository.findAll()).thenReturn(Flux.just(user, user2));

        StepVerifier.create(userService.getAllUsers())
                .expectNext(user)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(repository.findById(1L)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getUserById(1L))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getUserById_notFound() {
        Mockito.when(repository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserById(1L))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFound
                        && throwable.getMessage().equals("Usuario no encontrado"))
                .verify();
    }

    @Test
    void updateBalance() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(repository.findById(user.getId())).thenReturn(Mono.just(user));
        Mockito.when(repository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.updateBalance(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void updateBalance_notFound() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setBalance(100.0);

        Mockito.when(repository.findById(user.getId())).thenReturn(Mono.empty());

        StepVerifier.create(userService.updateBalance(user))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFound
                        && throwable.getMessage().equals("Usuario no encontrado"))
                .verify();
    }
}