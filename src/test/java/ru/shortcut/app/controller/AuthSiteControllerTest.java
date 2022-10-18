package ru.shortcut.app.controller;

import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.shortcut.app.model.dto.SignInDto;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.model.request.SiteRegistrationRequest;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuthSiteControllerTest {

    @InjectMocks
    private AuthSiteController authSiteController;

    @Mock
    private SiteService siteService;

    @Mock
    private SecretHelperConfigurator secretHelperConfigurator;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registrationTest() {
        var siteRegisterRequest = new SiteRegistrationRequest();
        siteRegisterRequest.setSite("https://test.ru/");
        var siteDto = SiteDto.builder()
                .siteName("https://test.ru/")
                .login("testUsername")
                .password("testPassword")
                .registerStatus(false)
                .build();
        Mockito.when(siteService.findBySite("https://test.ru/")).thenReturn(Optional.of(siteDto));

        var rsl = authSiteController.registration(siteRegisterRequest);

        Assert.assertNotNull(rsl.getBody());
        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(rsl.getBody().getLogin(), "testUsername");
        Assert.assertEquals(rsl.getBody().getPassword(), "testPassword");
        Assert.assertFalse(rsl.getBody().isRegistration());
    }

    @Test
    public void authorizationTest() {
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("108.25.34.15");
        var signInDto = new SignInDto();
        signInDto.setLogin("testUsername");
        signInDto.setPassword("testPassword");

        Mockito.when(siteService.existsByLoginAndPassword("testUsername", "testPassword")).thenReturn(
                Optional.of(SiteDto.builder().siteName("https://test.ru/").build())
        );

        Mockito.when(secretHelperConfigurator.generateToken("https://test.ru/", "108.25.34.15"))
                .thenReturn("testToken");

        var rsl = authSiteController.authorization(signInDto, request);

        Assert.assertNotNull(rsl.getBody());

        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(rsl.getBody(), "testToken");
    }

    @Test
    public void authorizationFail() {
        var signInDto = new SignInDto();
        signInDto.setLogin("testUsername");
        signInDto.setPassword("testPassword");
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("108.25.34.15");
        Mockito.when(siteService.existsByLoginAndPassword(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());


        var rsl = authSiteController.authorization(signInDto, request);

        Assert.assertNotNull(rsl.getBody());
        Assert.assertEquals(rsl.getBody(), "Неверный логин или пароль.");
        Assert.assertEquals(rsl.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}