package ru.shortcut.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.shortcut.app.service.DerivativeUrlService;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final DerivativeUrlService derivativeUrlService;

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirectByCode(@PathVariable("code") String code) {
        var subUrl = derivativeUrlService.findByCode(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, subUrl.getDerivativeUrl())
                .build();
    }
}
