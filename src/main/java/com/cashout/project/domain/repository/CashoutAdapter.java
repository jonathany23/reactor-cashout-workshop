package com.cashout.project.domain.repository;

import com.cashout.project.domain.entity.Cashout;
import com.cashout.project.exeptions.Error400Exception;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

public abstract class CashoutAdapter implements ICashoutRepository {

    private final ReactiveMongoTemplate template;

    public CashoutAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<Cashout> findCashoutByUserId(Long userId) {
        return template.find(Query.query(Criteria.where("userId").is(userId)), Cashout.class)
                .switchIfEmpty(Flux.error(new Error400Exception("Cashout not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new RuntimeException("Error getting cashout", e));
    }
}
