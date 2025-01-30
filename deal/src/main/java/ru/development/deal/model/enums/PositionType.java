package ru.development.deal.model.enums;

import java.util.Optional;

public enum PositionType {
    MID_MANAGER("Менеджер среднего звена"),
    TOP_MANAGER("Топ-менеджер"),
    OTHER("Другое"),
    WORKER("Наемный сотрудник"),
    OWNER("Владелец бизнеса");

    final String position;

    PositionType(String position) {
        this.position = position;
    }

    public static Optional<PositionType> convert(String positionType) {
        for (PositionType value : PositionType.values()) {
            if (value.getPosition().contains(positionType)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getPosition() {
        return position;
    }
}
