package com.example.auth.server.model;

import com.example.auth.server.model.entities.Credentials;
import com.example.auth.server.model.entities.ForbidenDomain;
import com.example.auth.server.model.repositories.CredentialsRepository;
import com.example.auth.server.model.repositories.ForbidenDomainRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * @autor Vincent
 * @date 28/07/2020
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PersistenceEngine {

    ForbidenDomainRepository forbidenDomains;


    CredentialsRepository userCredentials;


    public Credentials saveCredentials(Credentials credentials) {
        return userCredentials.save(credentials);
    }

    public Collection<ForbidenDomain> findAllDomains() {
        return forbidenDomains.findAll();
    }


    public Optional<Credentials> findCredentialsById(long idUser) {
        return userCredentials.findById(idUser);
    }

    public boolean existDomainByName(String domain) {
        return forbidenDomains.existsByDomain(domain.toLowerCase(Locale.ROOT));
    }

    public Optional<Credentials> findCredentialsByMail(String mailUser) {
        mailUser = mailUser.toLowerCase();
        return userCredentials.findByMail(mailUser);
    }









    public void cleanOldAndUnusedResetTokens(int ttl) {
        /*
       var td= resetPasswordTokens.findAll().stream()
                .filter(rpt-> rpt.getDateTime()
                        .plusSeconds(ttl)
                        .isBefore(LocalDateTime.now())
                        && rpt.getDateTimeUse()==null)
               .collect(Collectors.toList()).forEach(rpt -> rpt.setUser);
       */
    }


}
