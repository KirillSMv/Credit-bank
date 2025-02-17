package ru.development.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.development.deal.model.Client;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByPassportSeriesAndPassportNumber(String series, String number);

    Optional<Client> findByFirstNameAndLastNameAndMiddleNameAndBirthdate(String name, String lastName, String middleName,
                                                                         LocalDate birthdate);
}
