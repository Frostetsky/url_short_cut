package ru.shortcut.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shortcut.app.persistence.Site;

import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    public Optional<Site> findByLogin(String login);
    public Optional<Site> findBySite(String site);
    public Optional<Site> findByLoginAndPassword(String login, String password);
}
