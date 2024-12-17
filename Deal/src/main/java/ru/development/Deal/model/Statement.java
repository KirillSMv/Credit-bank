package ru.development.Deal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.development.Deal.model.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "statements")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementIdUuid;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_client_id_uuid", referencedColumnName = "clientIdUuid")
    private Client client;
    @OneToOne(cascade = CascadeType.ALL)
    private Credit credit;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @JdbcTypeCode(SqlTypes.JSON)
    private Offer appliedOffer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;
    private String ses; //todo типа данных
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatusHistory> statusHistory;
}
