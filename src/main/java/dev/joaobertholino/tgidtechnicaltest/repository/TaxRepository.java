package dev.joaobertholino.tgidtechnicaltest.repository;

import dev.joaobertholino.tgidtechnicaltest.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Integer> {
}
