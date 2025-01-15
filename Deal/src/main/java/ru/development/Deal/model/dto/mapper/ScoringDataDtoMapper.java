package ru.development.Deal.model.dto.mapper;


import org.springframework.stereotype.Component;
import ru.development.Deal.model.dto.FinishRegistrationRequestDto;
import ru.development.Deal.model.dto.ScoringDataDto;
import ru.development.Deal.model.entity.Statement;

@Component
public class ScoringDataDtoMapper {
    public ScoringDataDto toScoringDataDto(FinishRegistrationRequestDto dto, Statement statement) {
        return ScoringDataDto.builder()
                .amount(statement.getAppliedOffer().getRequestedAmount())
                .term(statement.getAppliedOffer().getTerm())
                .firstName(statement.getClient().getFirstName())
                .lastName(statement.getClient().getLastName())
                .middleName(statement.getClient().getMiddleName())
                .gender(dto.getGender())
                .employment(dto.getEmploymentDto())
                .birthdate(statement.getClient().getBirthdate())
                .passportSeries(statement.getClient().getPassport().getSeries())
                .passportNumber(statement.getClient().getPassport().getNumber())
                .passportIssueDate(dto.getPassportIssueDate())
                .passportIssueBranch(dto.getPassportIssueBranch())
                .dependentAmount(dto.getDependentAmount())
                .accountNumber(dto.getAccountNumber())
                .maritalStatus(dto.getMaritalStatus())
                .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(statement.getAppliedOffer().getIsSalaryClient())
                .build();
    }
}
