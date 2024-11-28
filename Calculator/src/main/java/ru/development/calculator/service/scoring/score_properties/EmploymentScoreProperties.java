package ru.development.calculator.service.scoring.score_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "employment-score")
@Getter
@Setter
@Component
public class EmploymentScoreProperties {
    private BigDecimal employmentStatusSubscoreRatio;
    private BigDecimal positionSubscoreRatio;
    private BigDecimal workExperienceSubscoreRatio;
    private BigDecimal selfEmployedCoef;
    private BigDecimal businessOwnerCoef;
    private BigDecimal employedCoef;
    private BigDecimal managerMiddleLevelCoef;
    private BigDecimal topManagerCoef;
    private BigDecimal otherPositionCoef;
    private Integer minTotalWorkExp;
    private Integer minCurrentWorkExp;
    private Integer minReliableTotalWorkExp;
    private Integer minReliableCurrentWorkExp;
    private BigDecimal lessMinReliableWorkExpCoef;
    private Integer reliableTotalWorkExp;
    private Integer reliableCurrentWorkExp;
    private BigDecimal reliableWorkExpCoef;
    private BigDecimal otherWorkExpCoef;
}
