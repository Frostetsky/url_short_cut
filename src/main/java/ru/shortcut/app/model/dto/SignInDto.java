package ru.shortcut.app.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SignInDto implements Serializable {

    private String login;

    private String password;
}
