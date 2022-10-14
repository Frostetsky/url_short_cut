package ru.shortcut.app.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticDto {

    private String url;

    private long total;

}
