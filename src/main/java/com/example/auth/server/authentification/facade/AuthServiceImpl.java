package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.PasswordEncoder;
import com.example.auth.server.authentification.facade.persistence.entities.StoreUser;
import com.example.auth.server.authentification.facade.persistence.repositories.UserRepository;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder bearersManager;

    @PostConstruct
    private void init() {
        fill();
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
        return s.toString();
    }

    private void fill() {
        var password = genPassword(16, 4);

        if (!userRepository.existsByMail(mailAdmin)) {

            var admin = new StoreUser(mailAdmin, PasswordEncoder.encode(password), Set.of("USER", "ADMIN"));
            userRepository.save(admin);
            System.out.println(mailAdmin + "  " + password);
        }
    }

    @Override
    public PublicKey publicKey() {
        return bearersManager.getPublicKey();
    }

    @Override
    public Bearers signIn(String mailUser, String passsword) throws MailAlreadyTakenException, BadPasswordFormat, InvalidMail {

        if (!passwordChecker.test(passsword))
            throw new BadPasswordFormat();
        if (!mailChecker.test(mailUser))
            throw new InvalidMail();

        mailUser = mailUser.toLowerCase();
        if (userRepository.existsByMail(mailUser))
            throw new MailAlreadyTakenException();

        Set<String> rolesOfUser = new TreeSet<>(BASE_ROLES);

        var u = new StoreUser(mailUser, PasswordEncoder.encode(passsword), rolesOfUser);
        var user = userRepository.save(u);

        return bearersManager.genBoth(user.getIdUser(), user.getRoles());
    }

    @Override
    public Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException {
        Optional<StoreUser> u = userRepository.findByMail(mail);
        if (u.isPresent()) {
            var user = u.get();
            if (!Arrays.equals(PasswordEncoder.encode(passsword), user.getPassword()))
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
            if (Arrays.equals(usr.getPassword(), PasswordEncoder.encode(password)))
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
            if (Arrays.equals(usr.getPassword(), PasswordEncoder.encode(oldPasssword))) {
                usr.setUpdateDate(LocalDateTime.now());
                usr.setPassword(PasswordEncoder.encode(newpasssword));
                userRepository.save(usr);
            } else throw new BadPasswordException();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long iduser, String password, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, BadPasswordException {
        Optional<StoreUser> user = userRepository.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (!Arrays.equals(usr.getPassword(), PasswordEncoder.encode(password)))
                throw new BadPasswordException();
            if (!mailChecker.test(newmail))
                throw new InvalidMail();
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
