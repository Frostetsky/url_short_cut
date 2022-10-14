package ru.shortcut.app.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SiteRegistrationResponse implements Serializable {

    private boolean registration;

    private String login;

    private String password;
}
