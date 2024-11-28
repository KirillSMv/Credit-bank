package ru.development.calculator.service.scoring.score_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "family-parameters-score")
@Getter
@Setter
@Component
public class FamilyParametersScoreProperties {
    private BigDecimal numberOfDependentScoreRatio;
    private BigDecimal maritalStatusScoreRatio;
    private BigDecimal marriedStatusCoef;
    private BigDecimal divorcedStatusCoef;
    private BigDecimal singleStatusCoef;
    private Integer highNumberOfDependents;
    private BigDecimal highNumberOfDependentsCoef;
    private Integer mediumNumberOfDependents;
    private BigDecimal mediumNumberOfDependentsCoef;
    private Integer lowNumberOfDependents;
    private BigDecimal lowNumberOfDependentsCoef;
    private BigDecimal noDependentCoef;
}
