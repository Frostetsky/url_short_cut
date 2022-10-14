package ru.shortcut.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.shortcut.app.ex.NoSuchValueException;
import ru.shortcut.app.model.dto.StatisticDto;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.service.StatisticService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.shortcut.app.util.SecretHelperConfigurator.TAG_AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class StatisticController {


    private final SecretHelperConfigurator secretHelperConfigurator;
    private final SiteService siteService;
    private final StatisticService statisticService;


    @GetMapping("/statistic")
    public List<StatisticDto> findAll(@RequestHeader(TAG_AUTHORIZATION) String token,
                                      HttpServletRequest httpServletRequest) {

        var siteName = secretHelperConfigurator.decodeTokenAndGetSite(token, httpServletRequest.getRemoteAddr());
        var siteDto = siteService.findBySite(siteName);

        return statisticService.getStatisticBySite(siteDto.orElseThrow(NoSuchValueException::new));
    }
}
