package ru.shortcut.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shortcut.app.persistence.DerivativeUrl;
import ru.shortcut.app.persistence.Site;

import java.util.List;

@Repository
public interface DerivativeUrlRepository extends JpaRepository<DerivativeUrl, Long> {

    @Query(
            nativeQuery = true,
            value = "UPDATE derivatives_urls SET call_count = call_count + 1 WHERE code = ?1 RETURNING derivative_url")
    String findByCode(String code);

    List<DerivativeUrl> findBySite(Site site);
}
