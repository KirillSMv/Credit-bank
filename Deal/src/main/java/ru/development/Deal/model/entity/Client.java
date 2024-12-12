package ru.development.Deal.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.development.Deal.model.enums.Gender;
import ru.development.Deal.model.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID clientIdUuid;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthdate;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    @OneToOne(cascade = CascadeType.ALL)
    private Passport passport;
    @OneToOne(cascade = CascadeType.ALL)
    private Employment employment;
    private String accountNumber;
}
