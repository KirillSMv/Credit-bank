package ru.development.gateway.model.enums;

import java.util.Optional;

public enum PositionType {
    MID_MANAGER("Менеджер среднего звена"),
    TOP_MANAGER("Топ-менеджер"),
    OTHER("Другое"),
    WORKER("Наемный сотрудник"),
    OWNER("Владелец бизнеса");

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
