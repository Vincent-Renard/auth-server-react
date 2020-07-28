package com.example.auth.server.authentification.facade.persistence;

import com.example.auth.server.authentification.facade.persistence.entities.Credentials;
import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomain;
import com.example.auth.server.authentification.facade.persistence.repositories.CredentialsRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
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
public class PersistenceEngine {

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    ForbidenDomainRepository domainRepository;


    @Autowired
    PasswordEncoder passwordEncoder;


    public String encodePassword(String clearPassword) {
        return passwordEncoder.encode(clearPassword);
    }


    public boolean credentialsExistsWithMail(String mail) {
        mail = mail.toLowerCase();
        return credentialsRepository.existsByMail(mail);
    }

    public Collection<Credentials> findAllCredentials() {
        return credentialsRepository.findAll();
    }

    public Credentials saveCredentials(Credentials credentials) {
        return credentialsRepository.save(credentials);
    }

    public Collection<ForbidenDomain> findAllDomains() {
        return domainRepository.findAll();
    }


    public ForbidenDomain saveDomain(ForbidenDomain forbidenDomain) {
        return domainRepository.save(forbidenDomain);
    }

    public void saveAllDomains(Collection<String> domains) {
        Set<String> deja = domainRepository.findAll()
                .stream()
                .map(ForbidenDomain::getDomain)
                .collect(Collectors.toSet());

        Set<ForbidenDomain> ds = domains
                .stream()
                .map(String::toLowerCase)
                .filter(d -> !deja.contains(d))
                .map(ForbidenDomain::new)
                .collect(Collectors.toSet());

        domainRepository.saveAll(ds);
    }

    public void deleteDomainById(String domain) {
        domain = domain.toLowerCase();
        domainRepository.deleteById(domain);
    }

    public Optional<Credentials> findCredentialsById(long idUser) {
        return credentialsRepository.findById(idUser);
    }

    public boolean existDomainByName(String domain) {
        domain = domain.toLowerCase();
        return domainRepository.existsByDomain(domain);
    }

    public Optional<Credentials> findCredentialsByMail(String mailUser) {
        mailUser = mailUser.toLowerCase();
        return credentialsRepository.findByMail(mailUser);
    }

    public boolean passwordMatches(String clearPassword, String encodedPassword) {
        return passwordEncoder.matches(clearPassword, encodedPassword);
    }

    public void deleteAllCredentials() {
        credentialsRepository.deleteAll();
    }

    public void deleteCredentialsById(long iduser) {
        credentialsRepository.deleteById(iduser);
    }
}
