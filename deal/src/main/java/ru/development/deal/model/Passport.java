package ru.development.deal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "passports")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passportIdUuid;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
