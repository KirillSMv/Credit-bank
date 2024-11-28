package ru.development.calculator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.development.calculator.validation.custom_annotations.AgeValidation;
import ru.development.calculator.validation.custom_annotations.MiddleNameValidation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {
    @Min(value = 20000, message = "Сумма кредита не может быть менее 20 000 рублей")
    @Max(value = 50000000, message = "Сумма кредита не может быть более 50 000 000 рублей, " +
            "пожалуйста, обратитесь в любое отделение банка для рассмотрения вашей заявки")
    @NotNull
    private BigDecimal amount;

    @Min(value = 6, message = "срок кредита не может быть менее 6 месяцев")
    @Max(value = 240, message = "срок кредита не может быть более 20 лет")
    @NotNull
    private Integer term;

    @Size(min = 2, max = 30, message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String firstName;

    @Size(min = 2, max = 30, message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String lastName;

    @Nullable
    @MiddleNameValidation(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String middleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @AgeValidation(message = "Заявитель должен быть старше 18 лет")
    private LocalDate birthdate;

    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String email;

    @Size(min = 4, max = 4, message = "Серия паспорта должна содержать 4 цифры")
    @Pattern(regexp = "[1-9]", message = "Серия паспорта должна содержать 4 цифры")
    @NotBlank(message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Size(min = 6, max = 6, message = "Номер паспорта должен содержать 6 цифр")
    @Pattern(regexp = "[1-9]", message = "Номер паспорта должен содержать 6 цифр")
    @NotBlank(message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}

