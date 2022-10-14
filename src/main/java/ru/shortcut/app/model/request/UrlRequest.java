package ru.shortcut.app.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UrlRequest implements Serializable {

    private String url;
}
