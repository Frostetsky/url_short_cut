package ru.shortcut.app.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DERIVATIVES_URLS")
@NoArgsConstructor
@Getter
@Setter
public class DerivativeUrl {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "DERIVATIVE_URL", unique = true, nullable = false)
    private String derivativeUrl;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "CALL_COUNT")
    @Version
    private long callCount;

    @ManyToOne
    @JoinColumn(name = "SITE_ID", nullable = false)
    private Site site;

    public DerivativeUrl(String derivativeUrl, String code, Site site) {
        this.derivativeUrl = derivativeUrl;
        this.code = code;
        this.site = site;
    }
}
