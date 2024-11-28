package ru.development.calculator.service.scoring.score_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "age-gender-score")
@Getter
@Setter
@Component
public class AgeGenderScoreProperties {
    private Integer minClientAgeScoring;
    private Integer maxClientAgeScoring;
    private Integer femaleMinHighReliableAge;
    private Integer femaleMaxHighReliableAge;
    private BigDecimal femaleReliableRangeCoef;
    private Integer maleMinHighReliableAge;
    private Integer maleMaxHighReliableAge;
    private BigDecimal maleReliableRangeCoef;
    private BigDecimal notBinaryGenderCoef;
}
