package dev.joaobertholino.tgidtechnicaltest.repository;

import dev.joaobertholino.tgidtechnicaltest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
