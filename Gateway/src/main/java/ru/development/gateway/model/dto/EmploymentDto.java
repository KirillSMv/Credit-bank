package ru.development.gateway.model.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.development.gateway.model.enums.EmploymentStatus;
import ru.development.gateway.model.enums.PositionType;
import ru.development.gateway.model.validation.custom_annotations.SalaryValidation;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {
    @Size(min = 10, max = 10, message = "Можно указать только 10 цифр латинского алфавита")
    @NotBlank(message = "Можно указать только 10 цифр латинского алфавита")
    @Pattern(regexp = "[0-9]+", message = "Можно указать только 10 цифр латинского алфавита")
    private String employerINN;

    @NotNull
    private EmploymentStatus employmentStatus;

    @SalaryValidation(message = "Размер заработной платы не может быть меньше минимального размера оплаты труда в Российской Федерации")
    @NotNull
    private BigDecimal salary;

    @Nullable
    private PositionType positionType;

    @Min(value = 0, message = "Пожалуйста, укажите опыт работы в месяцах")
    @Max(value = 1200, message = "Опыт работы не может превышать 100 лет")
    @NotNull
    private Integer workExperienceTotal;

    @Min(value = 0, message = "Пожалуйста, укажите опыт работы в месяцах")
    @Max(value = 1200, message = "Опыт работы не может превышать 100 лет")
    @NotNull
    private Integer workExperienceCurrent;
}
