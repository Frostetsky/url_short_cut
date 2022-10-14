package ru.shortcut.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SiteDto implements Serializable {

    @JsonIgnore
    private String siteName;

    @JsonIgnore
    private boolean registerStatus;

    @JsonIgnore
    private long id;

    @JsonIgnore
    private String login;

    @JsonIgnore
    private String password;
}
