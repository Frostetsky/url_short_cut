package ru.shortcut.app.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shortcut.app.model.dto.DerivativeUrlDto;
import ru.shortcut.app.model.response.UrlCodeResponse;
import ru.shortcut.app.model.request.UrlRequest;
import ru.shortcut.app.service.DerivativeUrlService;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.http.HttpServletRequest;

import static ru.shortcut.app.util.SecretHelperConfigurator.TAG_AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class RegistrationUrlController {

    private final DerivativeUrlService derivativeUrlService;
    private final SiteService siteService;
    private final SecretHelperConfigurator secretHelperConfigurator;

    @PostMapping("/convert")
    public ResponseEntity<UrlCodeResponse> convert(@RequestBody UrlRequest urlRequest,
                                                   @RequestHeader(TAG_AUTHORIZATION) String token,
                                                   HttpServletRequest httpServletRequest) {

        var site = secretHelperConfigurator.decodeTokenAndGetSite(token, httpServletRequest.getRemoteAddr());
        var siteDto = siteService.findBySite(site);

        if (!urlRequest.getUrl().contains(site)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return siteDto.map(name -> {
            var code = derivativeUrlService.save(
                    DerivativeUrlDto
                            .builder()
                            .derivativeUrl(urlRequest.getUrl())
                            .build(),
                    siteDto.get());

            return ResponseEntity.ok(UrlCodeResponse.builder()
                    .code(code)
                    .build());
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handlerConstraintViolationException() {
        return new ResponseEntity<>(
                "Данный url уже зарегистрирован в системе, используйте код выданный ранее.",
                HttpStatus.BAD_REQUEST);
    }
}
