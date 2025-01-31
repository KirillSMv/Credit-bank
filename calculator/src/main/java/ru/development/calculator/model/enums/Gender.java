package ru.development.calculator.model.enums;

import java.util.Optional;

public enum Gender {
    MALE("Мужчина"),
    FEMALE("Женщина"),
    NOTBINARY("Не бинарный");

    private final String str;

    Gender(String str) {
        this.str = str;
    }

    public static Optional<Gender> convert(String gender) {
        for (Gender value : Gender.values()) {
            if (value.getStr().equals(gender)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getStr() {
        return str;
    }
}
