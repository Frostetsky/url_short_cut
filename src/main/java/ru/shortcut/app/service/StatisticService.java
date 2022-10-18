package ru.shortcut.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.model.dto.StatisticDto;
import ru.shortcut.app.persistence.Site;
import ru.shortcut.app.repository.DerivativeUrlRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final DerivativeUrlRepository derivativeUrlRepository;

    public List<StatisticDto> getStatisticBySite(SiteDto siteDto) {
        var entitySite = new Site();
        entitySite.setId(siteDto.getId());
        entitySite.setLogin(siteDto.getLogin());
        entitySite.setPassword(siteDto.getPassword());
        entitySite.setSite(siteDto.getSiteName());
        return derivativeUrlRepository.findBySite(entitySite)
                .stream()
                .map(entity -> StatisticDto.builder()
                        .url(entity.getDerivativeUrl())
                        .total(entity.getCallCount())
                        .build())
                .collect(Collectors.toList());
    }
}
