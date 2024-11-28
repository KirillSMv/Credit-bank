package ru.development.calculator.model.enums;

import java.util.Optional;
import java.util.Set;

public enum MaritalStatus {
    MARRIED(Set.of("Женат", "Замужем")),
    DIVORCED(Set.of("Разведен", "Разведена")),
    SINGLE(Set.of("Не женат", "Не замужем"));

    private final Set<String> status;

    MaritalStatus(Set<String> status) {
        this.status = status;
    }

    public static Optional<MaritalStatus> convert(String status) {
        for (MaritalStatus value : MaritalStatus.values()) {
            if (value.getStatus().contains(status)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public Set<String> getStatus() {
        return status;
    }
}
