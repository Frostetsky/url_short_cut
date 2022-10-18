package ru.shortcut.app.controller;

import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.shortcut.app.model.dto.DerivativeUrlDto;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.model.request.UrlRequest;
import ru.shortcut.app.service.DerivativeUrlService;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RegistrationUrlControllerTest {

    @InjectMocks
    private RegistrationUrlController registrationUrlController;

    @Mock
    private DerivativeUrlService derivativeUrlService;

    @Mock
    private SiteService siteService;

    @Mock
    private SecretHelperConfigurator secretHelperConfigurator;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void convertTestNotContains() {
        var urlRequest = new UrlRequest();
        urlRequest.setUrl("https://test-other.ru/test/15");
        final var site = "https://test.ru/";

        Mockito.when(secretHelperConfigurator.decodeTokenAndGetSite("testToken", "182.12.10.14"))
                .thenReturn(site);

        Mockito.when(siteService.findBySite(site)).thenReturn(Optional.of(
                SiteDto.builder()
                        .siteName(site)
                        .password("testPassword")
                        .login("testUsername")
                        .build()
        ));

        var rsl = registrationUrlController.convert(
                urlRequest, "testToken", Mockito.mock(HttpServletRequest.class));

        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void convertTestCorrect() {
        var urlRequest = new UrlRequest();
        urlRequest.setUrl("https://test.ru/test/253");
        final var site = "https://test.ru/";
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("182.12.10.14");

        Mockito.when(secretHelperConfigurator.decodeTokenAndGetSite("testToken", "182.12.10.14"))
                .thenReturn(site);

        Mockito.when(siteService.findBySite(site)).thenReturn(Optional.of(
                SiteDto.builder()
                        .siteName(site)
                        .password("testPassword")
                        .login("testUsername")
                        .build()
        ));

        Mockito.when(derivativeUrlService.save(
                ArgumentMatchers.any(DerivativeUrlDto.class),
                ArgumentMatchers.any(SiteDto.class))).thenReturn("testCode");

        var rsl = registrationUrlController.convert(urlRequest, "testToken", request);

        Assert.assertNotNull(rsl.getBody());
        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(rsl.getBody().getCode(), "testCode");
    }
}