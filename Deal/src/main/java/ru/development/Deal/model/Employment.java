package ru.development.Deal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.development.Deal.model.enums.EmploymentStatus;
import ru.development.Deal.model.enums.PositionType;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "employments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employmentUuid;
    @Enumerated(value = EnumType.STRING)
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    @Enumerated(value = EnumType.STRING)
    private PositionType position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
