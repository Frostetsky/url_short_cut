package ru.shortcut.app.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.shortcut.app.model.dto.DerivativeUrlDto;
import ru.shortcut.app.service.DerivativeUrlService;

public class RedirectControllerTest {

    @InjectMocks
    private RedirectController redirectController;

    @Mock
    private DerivativeUrlService derivativeUrlService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void redirectTest() {
        Mockito.when(derivativeUrlService.findByCode("test-test-test")).thenReturn(
                DerivativeUrlDto.builder()
                        .derivativeUrl("https://job5ru.test/task/15/test")
                        .build()
        );

        var rsl = redirectController.redirectByCode("test-test-test");

        Assert.assertNotNull(rsl);
        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.FOUND);
        Assert.assertEquals(rsl.getHeaders().get(HttpHeaders.LOCATION).get(0), "https://job5ru.test/task/15/test");
    }
}