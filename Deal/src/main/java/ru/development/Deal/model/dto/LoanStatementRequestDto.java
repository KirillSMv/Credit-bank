package ru.development.Deal.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer term;
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String firstName;
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String lastName;
    @Nullable
    private String middleName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birthdate;
    @NotNull
    private String email;
    @NotBlank(message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;
    @NotBlank(message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}

