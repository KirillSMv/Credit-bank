package ru.development.Dossier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ui.context.Theme;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    private UUID statementId;
    private String address;
    private Theme theme;
    private String text;
}
