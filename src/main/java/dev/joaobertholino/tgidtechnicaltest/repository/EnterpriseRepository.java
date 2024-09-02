package dev.joaobertholino.tgidtechnicaltest.repository;

import dev.joaobertholino.tgidtechnicaltest.model.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Integer> {
}
