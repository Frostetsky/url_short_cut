package ru.shortcut.app.security.filter;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.shortcut.app.ex.ForbiddenException;
import ru.shortcut.app.service.SiteService;
import ru.shortcut.app.util.SecretHelperConfigurator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecretHelperConfigurator secretHelperConfigurator;
    private final SiteService siteService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  SecretHelperConfigurator secretHelperConfigurator,
                                  SiteService siteService) {
        super(authenticationManager);
        this.secretHelperConfigurator = secretHelperConfigurator;
        this.siteService = siteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        var token = req.getHeader(SecretHelperConfigurator.TAG_AUTHORIZATION);
        if (isNotValid(token, req.getRemoteAddr())) {
            res.setStatus(HttpStatus.FORBIDDEN.value());
            chain.doFilter(req, res);
            return;
        }

        var authentication = getAuthentication(token, req.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token, String ipAddress) {
        var site = secretHelperConfigurator.decodeTokenAndGetSite(token, ipAddress);
        return siteService.findBySite(site)
                .map(cred -> new UsernamePasswordAuthenticationToken(
                        cred.getLogin(),
                        cred.getPassword(),
                        Collections.emptyList()))
                .orElseThrow(() -> new ForbiddenException("Access denied"));
    }

    private boolean isNotValid(String token, String ipAddress) {
        Predicate<String> isNull = Objects::isNull;
        Predicate<String> notBearer = t -> !t.startsWith(secretHelperConfigurator.getTokenType());
        Predicate<String> isExpired = t -> secretHelperConfigurator.isExpired(t, ipAddress);
        return (isNull.or(notBearer).or(isExpired)).test(token);
    }
}
