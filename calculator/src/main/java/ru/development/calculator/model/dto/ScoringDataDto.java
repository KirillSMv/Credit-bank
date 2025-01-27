package ru.development.calculator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.validation.custom_annotations.AccountNumberValidation;
import ru.development.calculator.validation.custom_annotations.AgeValidation;
import ru.development.calculator.validation.custom_annotations.MiddleNameValidation;
import ru.development.calculator.validation.custom_annotations.PassportDateValidation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDto {
    private BigDecimal amount;

    @Min(value = 6, message = "срок кредита не может быть менее {value} месяцев")
    @Max(value = 240, message = "срок кредита не может быть более {value} лет")
    @NotNull
    private Integer term;

    @Size(min = 2, max = 30, message = "Можно указать только от {min} до {max} букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]+", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String firstName;

    @Size(min = 2, max = 30, message = "Можно указать только от {min} до {max} букв латинского алфавита")
    @NotBlank(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    @Pattern(regexp = "[a-zA-Z]+", message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String lastName;

    @Nullable
    @MiddleNameValidation(message = "Можно указать только от 2 до 30 букв латинского алфавита")
    private String middleName;

    @NotNull
    private Gender gender;

    @Valid
    @NotNull
    private EmploymentDto employment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @AgeValidation(message = "Заявитель должен быть старше 18 лет")
    @NotNull
    private LocalDate birthdate;

    @Size(min = 4, max = 4, message = "Серия паспорта должна содержать {min} цифры")
    @Pattern(regexp = "[0-9]+", message = "Серия паспорта должна содержать 4 цифры")
    @NotBlank(message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Size(min = 6, max = 6, message = "Номер паспорта должен содержать {min} цифр")
    @Pattern(regexp = "[0-9]+", message = "Номер паспорта должен содержать 6 цифр")
    @NotBlank(message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Пожалуйста, проверьте правильность указанной даты выдачи паспорта")
    @PassportDateValidation(message = "Пожалуйста, проверьте правильность указанной даты выдачи паспорта")
    private LocalDate passportIssueDate;

    @Size(min = 4, max = 150, message = "Пожалуйста, проверьте правильность указанной информации, максимум {max} символов")
    @NotBlank(message = "Пожалуйста, укажите орган, выдавший паспорт")
    @Pattern(regexp = "[a-z0-9A-Z]+", message = "Можно указать только буквы латинского алфавита или цифры")
    private String passportIssueBranch;

    @Min(value = 0, message = "Пожалуйста, проверьте правильность указанных данных")
    @Max(value = 15, message = "Пожалуйста, проверьте корректность указанных данных. Укажите количество иждивенцев")
    @NotNull
    private Integer dependentAmount;

    @AccountNumberValidation(message = "Пожалуйста, укажите номер вашего счета - 20 цифр без пробелов")
    @Nullable
    private String accountNumber;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;
}
