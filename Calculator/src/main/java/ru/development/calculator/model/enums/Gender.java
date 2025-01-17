package ru.development.calculator.model.enums;

import java.util.Optional;

public enum Gender {
    MALE("Мужчина"),
    FEMALE("Женщина"),
    NOTBINARY("Не бинарный");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public static Optional<Gender> convert(String gender) {
        for (Gender value : Gender.values()) {
            if (value.getGender().equals(gender)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getGender() {
        return gender;
    }
}
