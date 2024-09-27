package com.cashout.project.controller;

import com.cashout.project.controller.dto.UpdateBalanceDto;
import com.cashout.project.domain.entity.User;
import com.cashout.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    public Mono<User> createUser(@Valid @RequestBody User user) {
        return service.createUser(user);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PutMapping("/{id}/balance")
    public Mono<User> updateBalance(@PathVariable Long id, @RequestBody UpdateBalanceDto balance) {
        User user = new User();
        user.setId(id);
        user.setBalance(balance.getBalance());
        return service.updateBalance(user);
    }
}
