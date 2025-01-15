package ru.development.Deal.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.development.Deal.model.dto.PaymentScheduleElementDto;
import ru.development.Deal.model.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID creditIdUuid;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentScheduleElementDto> paymentSchedule;
    private Boolean insuranceEnabled;
    private Boolean salaryClient;
    @Enumerated(value = EnumType.STRING)
    private CreditStatus creditStatus = CreditStatus.CALCULATED;
}
