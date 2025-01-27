package ru.development.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.development.gateway.model.enums.Gender;
import ru.development.gateway.model.enums.MaritalStatus;
import ru.development.gateway.model.validation.custom_annotations.AccountNumberValidation;
import ru.development.gateway.model.validation.custom_annotations.PassportDateValidation;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDto {
    @NotNull
    private Gender gender;
    @NotNull
    private MaritalStatus maritalStatus;
    @Min(value = 0, message = "Пожалуйста, проверьте правильность указанных данных")
    @Max(value = 15, message = "Пожалуйста, проверьте корректность указанных данных. Укажите количество иждивенцев")
    @NotNull
    private Integer dependentAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Пожалуйста, проверьте правильность указанной даты выдачи паспорта")
    @PassportDateValidation(message = "Пожалуйста, проверьте правильность указанной даты выдачи паспорта")
    private LocalDate passportIssueDate;
    @Size(min = 4, max = 150, message = "Пожалуйста, проверьте правильность указанной информации, максимум {max} символов")
    @NotBlank(message = "Пожалуйста, укажите орган, выдавший паспорт")
    @Pattern(regexp = "[a-z0-9A-Z]+", message = "Можно указать только буквы латинского алфавита или цифры")
    private String passportIssueBranch;
    private EmploymentDto employmentDto;
    @AccountNumberValidation(message = "Пожалуйста, укажите номер вашего счета - 20 цифр без пробелов")
    @Nullable
    private String accountNumber;
}
