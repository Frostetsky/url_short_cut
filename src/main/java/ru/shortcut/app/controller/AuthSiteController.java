package ru.shortcut.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shortcut.app.model.dto.SignInDto;
import ru.shortcut.app.model.request.SiteRegistrationRequest;
import ru.shortcut.app.model.response.SiteRegistrationResponse;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthSiteController {

    private final SiteService siteService;
    private final SecretHelperConfigurator secretHelperConfigurator;

    @PostMapping("/registration")
    public ResponseEntity<SiteRegistrationResponse> registration(@RequestBody SiteRegistrationRequest request) {
        var registerSiteRsl = siteService.findBySite(request.getSite())
                .orElseGet(() -> siteService.addOrUpdate(request.getSite()));

        return ResponseEntity.ok(SiteRegistrationResponse.builder()
                        .registration(registerSiteRsl.isRegisterStatus())
                        .login(registerSiteRsl.getLogin())
                        .password(registerSiteRsl.getPassword())
                        .build());
    }

    @PostMapping("/authorization")
    public ResponseEntity<String> authorization(@RequestBody SignInDto signInDto,
                                                HttpServletRequest httpServletRequest) {

        var site = siteService.existsByLoginAndPassword(signInDto.getLogin(), signInDto.getPassword());

        return site.map(value -> ResponseEntity.ok(secretHelperConfigurator.generateToken(
                value.getSiteName(), httpServletRequest.getRemoteAddr())))
                .orElseGet(() -> new ResponseEntity<>("Неверный логин или пароль.", HttpStatus.UNAUTHORIZED));
    }
}
