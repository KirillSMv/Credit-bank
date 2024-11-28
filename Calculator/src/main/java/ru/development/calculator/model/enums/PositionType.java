package ru.development.calculator.model.enums;

import java.util.Optional;

public enum PositionType {
    MANAGERMIDDLELEVEL("Менеджер среднего звена"),
    TOPMANAGER("Топ-менеджер"),
    OTHER("Другое");

    final String positionType;

    PositionType(String positionType) {
        this.positionType = positionType;
    }

    public static Optional<PositionType> convert(String positionType) {
        for (PositionType value : PositionType.values()) {
            if (value.getPositionType().contains(positionType)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getPositionType() {
        return positionType;
    }
}
