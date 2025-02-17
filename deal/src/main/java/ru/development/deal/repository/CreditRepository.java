package ru.development.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.development.deal.model.Credit;

import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
