package ru.shortcut.app.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SiteRegistrationRequest implements Serializable {

    private String site;
}
