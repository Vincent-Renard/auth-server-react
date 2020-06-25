package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.facade.persistence.entities.ForbidenDomainEntity;
import com.example.auth.server.authentification.facade.persistence.entities.StoreUser;
import com.example.auth.server.authentification.facade.persistence.repositories.ForbidenDomainRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.UserRepository;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@Service
public class AuthServiceImpl implements AuthService, AuthUtils {


    private final String mailAdmin = "admin@admin.com";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForbidenDomainRepository domainRepository;

    @Autowired
    private JwtEncoder bearersManager;

    private Set<String> domainsNotAllowed;

    @PostConstruct
    private void init() {

        fill();
        updateInternDomains();

    }

    private void updateInternDomains() {
        domainsNotAllowed = domainRepository.findAll()
                .stream()
                .map(ForbidenDomainEntity::getDomain)
                .collect(Collectors.toSet());
    }

    private String genPassword(int lenght, int chunks) {
        String sep = "-";
        String alphaU = "ABCDEGHIJKLMNOPQRSTUVWXYZ";
        String alpha = alphaU + alphaU.toLowerCase() + "0123456789";
        alpha = alpha.replace("0", "")
                .replace("O", "");
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i <= lenght; i++) {
            s.append(alpha.charAt(r.nextInt(alpha.length())));
            if (i > 0 && i % chunks == 0 && i < lenght) {
                s.append(sep);
            }

        }
        //return s.toString();
        return "pTWER-gYG5-ohom-qsZg";
    }

    private void fill() {
        var password = genPassword(16, 4);

        if (!userRepository.existsByMail(mailAdmin)) {

            var admin = new StoreUser(mailAdmin, passwordEncoder.encode(password), Set.of("USER", "ADMIN"));
            userRepository.save(admin);
            System.out.println(mailAdmin + "  " + password);
        }
    }

    @Override
    public PublicKey publicKey() {
        return bearersManager.getPublicKey();
    }

    @Override
    public ForbidenDomainEntity addForbidenDomain(String domain) {
        domain = domain.toLowerCase();
        if (!domainsNotAllowed.contains(domain)) {
            domainRepository.save(new ForbidenDomainEntity(domain));
            updateInternDomains();
        }

        return domainRepository.getOne(domain);
    }

    @Override
    public void delForbidenDomain(String domain) {
        domain = domain.toLowerCase();

        if (domainsNotAllowed.contains(domain)) {

            domainRepository.deleteById(domain);
            domainsNotAllowed.remove(domain);
            updateInternDomains();
        }
    }

    @Override
    public Collection<ForbidenDomainEntity> getAllDomainNotAllowed() {

        return domainRepository.findAll();
    }

    @Override
    public Bearers signIn(String mailUser, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail, ForbidenDomainMailUse {
        mailUser = mailUser.toLowerCase();
        if (!passwordChecker.test(passsword))
            throw new BadPasswordFormat();
        if (!mailChecker.test(mailUser))
            throw new InvalidMail();
        String domain = mailUser.split("@")[1];
        if (domainsNotAllowed.contains(domain))
            throw new ForbidenDomainMailUse();


        if (userRepository.existsByMail(mailUser))
            throw new MailAlreadyTakenException();

        Set<String> rolesOfUser = new TreeSet<>(BASE_ROLES);

        var u = new StoreUser(mailUser, passwordEncoder.encode(passsword), rolesOfUser);
        var user = userRepository.save(u);

        return bearersManager.genBoth(user.getIdUser(), user.getRoles());
    }

    @Override
    public Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException {
        mail = mail.toLowerCase();
        Optional<StoreUser> u = userRepository.findByMail(mail);
        if (u.isPresent()) {
            var user = u.get();
            if (!passwordEncoder.matches(passsword, user.getPassword()))
                throw new BadPasswordException();

            return bearersManager.genBoth(user.getIdUser(), user.getRoles());
        } else throw new NotSuchUserException();

    }

    @Override
    public Bearers refresh(long iduser) throws NotSuchUserException {

        Optional<StoreUser> user = userRepository.findById(iduser);
        if (user.isPresent()) {
            return bearersManager.genBoth(iduser, user.get().getRoles());

        } else {
            throw new NotSuchUserException();
        }

    }

    @Override
    public void clear() {
        userRepository.deleteAll();
        fill();
    }

    @Override
    public void signOut(long iduser, String password) throws BadPasswordException, NotSuchUserException {
        Optional<StoreUser> user = userRepository.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (passwordEncoder.matches(password, usr.getPassword()))
                userRepository.deleteById(iduser);
            else throw new BadPasswordException();

        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public StoreUser updateRoles(long iduser, Collection<String> newRoles) throws NotSuchUserException {
        Optional<StoreUser> user = userRepository.findById(iduser);
        newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());
        if (!user.isPresent()) {
            throw new NotSuchUserException();
        } else {
            if (POSSILBES_ROLES.containsAll(newRoles)) {
                var usr = user.get();
                usr.setUpdateDate(LocalDateTime.now());
                usr.setRoles(newRoles);
                usr = userRepository.save(usr);
                return usr;

            }
        }
        return user.get();
    }

    @Override
    public void updatePassword(long iduser, String oldPasssword, String newpasssword) throws NotSuchUserException, BadPasswordException, BadPasswordFormat {
        Optional<StoreUser> user = userRepository.findById(iduser);

        if (user.isPresent()) {
            var usr = user.get();
            if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormat();
            if (passwordEncoder.matches(oldPasssword, usr.getPassword())) {
                usr.setUpdateDate(LocalDateTime.now());
                usr.setPassword(passwordEncoder.encode(newpasssword));
                userRepository.save(usr);
            } else throw new BadPasswordException();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long iduser, String password, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, BadPasswordException, ForbidenDomainMailUse {
        Optional<StoreUser> user = userRepository.findById(iduser);

        if (user.isPresent()) {
            newmail = newmail.toLowerCase();
            var usr = user.get();
            if (!passwordEncoder.matches(password, usr.getPassword()))
                throw new BadPasswordException();
            if (!mailChecker.test(newmail))
                throw new InvalidMail();
            String domain = newmail.split("@")[1];
            if (domainsNotAllowed.contains(domain))
                throw new ForbidenDomainMailUse();

            if (userRepository.existsByMail(newmail))
                throw new MailAlreadyTakenException();

            if (usr.getMail().equals(mailAdmin))
                newmail = mailAdmin;

            usr.setMail(newmail);
            usr.setUpdateDate(LocalDateTime.now());
            userRepository.save(usr);

        } else {
            throw new NotSuchUserException();
        }
    }
}
