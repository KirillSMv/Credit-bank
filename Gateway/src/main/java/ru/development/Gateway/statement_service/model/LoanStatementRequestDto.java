package ru.development.Gateway.statement_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.development.Gateway.statement_service.validation.custom_annotations.AgeValidation;
import ru.development.Gateway.statement_service.validation.custom_annotations.MiddleNameValidation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatementRequestDto {
    @Min(value = 20000, message = "Сумма кредита не может быть менее {value} рублей")
    @Max(value = 100000000, message = "Сумма кредита не может быть более {value} рублей, " +
            "пожалуйста, обратитесь в любое отделение банка для рассмотрения вашей заявки")
    @NotNull
    private BigDecimal amount;

    @Min(value = 6, message = "Срок кредита не может быть менее {value} месяцев")
    @Max(value = 240, message = "Срок кредита не может быть более {value} лет")
    @NotNull
    private Integer term;

    @Size(min = 2, max = 30, message = "В имени можно указать только от {min} до {max} букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]+", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String firstName;

    @Size(min = 2, max = 30, message = "В фамилии можно указать только от {min} до {max} букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]+", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String lastName;

    @Nullable
    @MiddleNameValidation(message = "В отчестве можно указать только от 2 до 30 букв латинского алфавита")
    private String middleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @AgeValidation(message = "Заявитель должен быть старше 18 лет")
    @NotNull
    private LocalDate birthdate;

    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$",
            message = "Пожалуйста, проверьте правильность введенного адреса электронной почты")
    @NotNull
    private String email;

    @Size(min = 4, max = 4, message = "Серия паспорта должна содержать {min} цифры")
    @Pattern(regexp = "[0-9]+", message = "Серия паспорта должна содержать 4 цифры")
    @NotBlank(message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Size(min = 6, max = 6, message = "Номер паспорта должен содержать {min} цифр")
    @Pattern(regexp = "[0-9]+", message = "Номер паспорта должен содержать 6 цифр")
    @NotBlank(message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}
