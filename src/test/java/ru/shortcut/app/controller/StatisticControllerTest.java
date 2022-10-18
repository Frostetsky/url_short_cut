package ru.shortcut.app.controller;

import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.model.dto.StatisticDto;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.service.StatisticService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class StatisticControllerTest {

    @InjectMocks
    private StatisticController statisticController;

    @Mock
    private SiteService siteService;

    @Mock
    private StatisticService statisticService;

    @Mock
    private SecretHelperConfigurator secretHelperConfigurator;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getStatistic() {
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("182.15.12.3");
        Mockito.when(secretHelperConfigurator.decodeTokenAndGetSite("testToken", "182.15.12.3"))
                .thenReturn("https://test.ru/");
        Mockito.when(siteService.findBySite("https://test.ru/")).thenReturn(Optional.of(SiteDto.builder().build()));

        Mockito.when(statisticService.getStatisticBySite(ArgumentMatchers.any(SiteDto.class))).thenReturn(
                List.of(StatisticDto.builder()
                                .total(10)
                                .url("https://test.ru/test/15")
                        .build(),
                        StatisticDto.builder()
                                .total(2)
                                .url("https://test.ru/task/25")
                        .build())
        );

        var rsl = statisticController.findAllStatisticBySite("testToken", request);
        Assert.assertEquals(rsl.size(), 2);
        Assert.assertEquals(rsl.get(0).getTotal(), 10);
        Assert.assertEquals(rsl.get(0).getUrl(), "https://test.ru/test/15");
        Assert.assertEquals(rsl.get(1).getTotal(), 2);
        Assert.assertEquals(rsl.get(1).getUrl(), "https://test.ru/task/25");
    }

}