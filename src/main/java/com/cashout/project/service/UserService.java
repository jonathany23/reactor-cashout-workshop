package com.cashout.project.service;

import com.cashout.project.domain.entity.User;
import com.cashout.project.domain.repository.IUserRepository;
import com.cashout.project.exeptions.Error400Exception;
import com.cashout.project.exeptions.UserNotFound;
import com.cashout.project.service.gateway.IUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository){
        this.repository = repository;
    }

    @Override
    public Mono<User> createUser(User user) {
        return repository.save(user)
                .onErrorMap(throwable -> new Error400Exception("Error al guardar el usuario"));
    }

    @Override
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFound("Usuario no encontrado")));
    }

    @Override
    public Mono<User> updateBalance(User user) {
        return repository.findById(user.getId())
                .switchIfEmpty(Mono.error(new UserNotFound("Usuario no encontrado")))
                .flatMap(user1 -> {
                    user1.setBalance(user.getBalance());
                    return repository.save(user1);
                });
    }
}
