package ru.shortcut.app.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UrlCodeResponse implements Serializable {

    private String code;
}
