package ru.shortcut.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shortcut.app.model.dto.SiteDto;
import ru.shortcut.app.persistence.Site;
import ru.shortcut.app.repository.SiteRepository;
import ru.shortcut.app.util.SecretHelperConfigurator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteService implements UserDetailsService {

    private final SiteRepository siteRepository;

    private final SecretHelperConfigurator secretHelperConfigurator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return siteRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<SiteDto> existsByLoginAndPassword(String login, String password) {
        return siteRepository.findByLoginAndPassword(login, password)
                .map(entity -> SiteDto.builder()
                        .siteName(entity.getSite())
                        .build());
    }

    public Optional<SiteDto> findBySite(String site) {
        return siteRepository.findBySite(site)
                .map(entity -> SiteDto.builder()
                        .siteName(entity.getSite())
                        .login(entity.getLogin())
                        .password(entity.getPassword())
                        .id(entity.getId())
                        .build());
    }

    public SiteDto addOrUpdate(String site) {
        var entitySite = new Site();
        entitySite.setSite(site);
        entitySite.setLogin(secretHelperConfigurator.generateLogin());
        entitySite.setPassword(secretHelperConfigurator.generatePassword());
        var addedSite = siteRepository.save(entitySite);
        return SiteDto.builder()
                .siteName(addedSite.getSite())
                .registerStatus(true)
                .login(addedSite.getLogin())
                .password(addedSite.getPassword())
                .build();
    }
}
