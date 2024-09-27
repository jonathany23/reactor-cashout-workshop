package com.cashout.project.domain.repository;

import com.cashout.project.domain.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IUserRepository extends ReactiveCrudRepository<User, Long> {
}
