package ru.development.dossier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto implements Serializable {
    private UUID statementId;
    private String address;
    private Theme theme;
    private String text;
}
