package dev.joaobertholino.tgidtechnicaltest.repository;

import dev.joaobertholino.tgidtechnicaltest.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
