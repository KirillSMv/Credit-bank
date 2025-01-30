package ru.development.calculator.model.enums;

import java.util.Optional;

public enum EmploymentStatus {
    UNEMPLOYED("Безработный"),
    SELF_EMPLOYED("Самозанятый"),
    BUSINESS_OWNER("Владелец бизнеса"),
    EMPLOYED("Трудоустроен");

    private final String status;

    EmploymentStatus(String status) {
        this.status = status;
    }

    public static Optional<EmploymentStatus> convert(String employmentStatus) {
        for (EmploymentStatus value : EmploymentStatus.values()) {
            if (value.getStatus().contains(employmentStatus)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getStatus() {
        return status;
    }
}
