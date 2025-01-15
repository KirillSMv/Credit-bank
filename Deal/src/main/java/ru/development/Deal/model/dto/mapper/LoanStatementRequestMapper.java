package ru.development.Deal.model.dto.mapper;

import org.springframework.stereotype.Component;
import ru.development.Deal.model.dto.LoanStatementRequestDto;
import ru.development.Deal.model.entity.Client;
import ru.development.Deal.model.entity.Passport;
import ru.development.Deal.model.entity.Statement;
import ru.development.Deal.model.entity.StatusHistory;
import ru.development.Deal.model.enums.ApplicationStatus;
import ru.development.Deal.model.enums.ChangeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanStatementRequestMapper {
    public Client toClient(LoanStatementRequestDto dto) {
        return Client.builder()
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthdate(dto.getBirthdate())
                .email(dto.getEmail())
                .passport(toPassport(dto))
                .build();
    }

    public Statement toStatement(LoanStatementRequestDto dto) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Statement.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(currentTime)
                .client(toClient(dto))
                .statusHistory(new ArrayList<>(List.of(StatusHistory.builder()
                        .status(ApplicationStatus.PREAPPROVAL)
                        .time(currentTime)
                        .changeType(ChangeType.AUTOMATIC)
                        .build())))
                .build();
    }


    public Passport toPassport(LoanStatementRequestDto dto) {
        return Passport.builder()
                .series(dto.getPassportSeries())
                .number(dto.getPassportNumber())
                .build();
    }
}

/*


    private Gender gender;
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;
    @OneToOne(cascade = CascadeType.ALL)
    private Passport passport;
    @OneToOne(cascade = CascadeType.ALL)
    private Employment employment;
    private String accountNumber;
 */
