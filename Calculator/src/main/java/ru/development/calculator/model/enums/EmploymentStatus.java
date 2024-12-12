package ru.development.calculator.model.enums;

import java.util.Optional;

public enum EmploymentStatus {
    UNEMPLOYED("Безработный"),
    SELF_EMPLOYED("Самозанятый"),
    BUSINESS_OWNER("Владелец бизнеса"),
    EMPLOYED("Трудоустроен");

    private final String employmentStatus;

    EmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public static Optional<EmploymentStatus> convert(String employmentStatus) {
        for (EmploymentStatus value : EmploymentStatus.values()) {
            if (value.getEmploymentStatus().contains(employmentStatus)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }
}
