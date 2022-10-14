package ru.shortcut.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shortcut.app.ex.NoSuchValueException;
import ru.shortcut.app.model.dto.DerivativeUrlDto;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.model.dto.StatisticDto;
import ru.shortcut.app.persistence.DerivativeUrl;
import ru.shortcut.app.persistence.Site;
import ru.shortcut.app.repository.DerivativeUrlRepository;
import ru.shortcut.app.util.SecretHelperConfigurator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DerivativeUrlService {

    private final DerivativeUrlRepository derivativeUrlRepository;

    private final SecretHelperConfigurator secretHelperConfigurator;

    public String save(DerivativeUrlDto derivativeUrlDto, SiteDto siteDto) {
        var entityDerUrl = new DerivativeUrl();
        var code = secretHelperConfigurator.generateCode();
        var entitySite = new Site();
        entitySite.setSite(siteDto.getSiteName());
        entitySite.setId(siteDto.getId());
        entitySite.setLogin(siteDto.getLogin());
        entitySite.setPassword(siteDto.getPassword());
        entityDerUrl.setDerivativeUrl(derivativeUrlDto.getDerivativeUrl());
        entityDerUrl.setCode(code);
        entityDerUrl.setSite(entitySite);
        return derivativeUrlRepository.save(entityDerUrl).getCode();
    }

    public DerivativeUrlDto findByCode(String code) {
        return Optional.ofNullable(derivativeUrlRepository.findByCode(code))
                .map(v -> DerivativeUrlDto.builder()
                        .derivativeUrl(v)
                        .build())
                .orElseThrow(NoSuchValueException::new);
    }
}
