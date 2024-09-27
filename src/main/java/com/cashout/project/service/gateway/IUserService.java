package com.cashout.project.service.gateway;

import com.cashout.project.domain.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> createUser(User user);
    Flux<User> getAllUsers();
    Mono<User> getUserById(Long id);
    Mono<User> updateBalance(User user);
}
