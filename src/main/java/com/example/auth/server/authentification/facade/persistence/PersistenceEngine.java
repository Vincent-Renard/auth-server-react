package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 28/07/2020
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersistenceEngine {

    @Autowired
    CredentialsRepository userCredentials;

    @Autowired
    ForbidenDomainRepository forbidenDomains;


    @Autowired
    PasswordEncoder passwordEncoder;


    public String encodePassword(String clearPassword) {
        return passwordEncoder.encode(clearPassword);
    }


    public boolean credentialsExistsWithMail(String mail) {
        mail = mail.toLowerCase();
        return userCredentials.existsByMail(mail);
    }

    public Collection<Credentials> findAllCredentials() {
        return userCredentials.findAll();
    }

    public Credentials saveCredentials(Credentials credentials) {
        return userCredentials.save(credentials);
    }

    public Collection<ForbidenDomain> findAllDomains() {
        return forbidenDomains.findAll();
    }


    public ForbidenDomain saveDomain(ForbidenDomain forbidenDomain) {
        return forbidenDomains.save(forbidenDomain);
    }

    public void saveAllDomains(Collection<String> domains) {
        Set<String> deja = forbidenDomains.findAll()
                .stream()
                .map(ForbidenDomain::getDomain)
                .collect(Collectors.toSet());

        Set<ForbidenDomain> ds = domains
                .stream()
                .map(String::toLowerCase)
                .filter(d -> !deja.contains(d))
                .map(ForbidenDomain::new)
                .collect(Collectors.toSet());

        forbidenDomains.saveAll(ds);
    }

    public void deleteDomainById(String domain) {
        domain = domain.toLowerCase();
        forbidenDomains.deleteById(domain);
    }

    public Optional<Credentials> findCredentialsById(long idUser) {
        return userCredentials.findById(idUser);
    }

    public boolean existDomainByName(String domain) {
        domain = domain.toLowerCase();
        return forbidenDomains.existsByDomain(domain);
    }

    public Optional<Credentials> findCredentialsByMail(String mailUser) {
        mailUser = mailUser.toLowerCase();
        return userCredentials.findByMail(mailUser);
    }

    public boolean passwordMatches(String clearPassword, String encodedPassword) {
        return passwordEncoder.matches(clearPassword, encodedPassword);
    }

    public void deleteAllCredentials() {
        userCredentials.deleteAll();
    }

    public void deleteCredentialsById(long iduser) {
        userCredentials.deleteById(iduser);
    }


}
