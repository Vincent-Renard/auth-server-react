package com.example.auth.server.authentification.facade;

import com.example.auth.server.authentification.PasswordEncoder;
import com.example.auth.server.authentification.facade.persistence.entities.StoreUser;
import com.example.auth.server.authentification.facade.persistence.repositories.UserRepository;
import com.example.auth.server.authentification.facade.persistence.repositories.UserRepositoryInMemory;
import com.example.auth.server.authentification.token.manager.JwtEncoder;
import com.example.auth.server.model.dtos.out.Bearers;
import com.example.auth.server.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @autor Vincent
 * @date 12/03/2020
 */
@Service
public class AuthServiceImpl implements AuthService, AuthUtils {

    private final Predicate<String> passwordChecker = password -> password.length() > MIN_LENGHT_PASSWORD && password.length() < MAX_LENGHT_PASSWORD;
    private final Predicate<String> mailChecker = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).asPredicate();

    private final UserRepository users;

    @Autowired
    private JwtEncoder bearersManager;

    public AuthServiceImpl() {
        users = new UserRepositoryInMemory();

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

        System.out.println("admin@admin.com  " + password);

        var admin = new StoreUser("admin@admin.com", PasswordEncoder.encode(password), Set.of("ADMIN", "USER"));
        try {
            users.store(admin);
        } catch (ValueCreatingError ignored) {

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

        Set<String> rolesOfUser = Set.of("USER");
        var u = new StoreUser(mailUser, PasswordEncoder.encode(passsword), rolesOfUser);
        try {
            long id = users.store(u);
            return bearersManager.genBoth(id, u.getRoles());
        } catch (ValueCreatingError valueCreatingError) {
            throw new MailAlreadyTakenException();
        }



    }

    @Override
    public Bearers logIn(String mail, String passsword) throws BadPasswordException, NotSuchUserException {
        Optional<StoreUser> u = users.findByMail(mail);
        if (u.isPresent()) {
            var user = u.get();
            if (!Arrays.equals(PasswordEncoder.encode(passsword), user.getPassword()))
                throw new BadPasswordException();

            return bearersManager.genBoth(user.getIdUser(), user.getRoles());
        } else throw new NotSuchUserException();

    }

    @Override
    public Bearers refresh(long iduser) throws NotSuchUserException {

        Optional<StoreUser> user = users.findById(iduser);
        if (user.isPresent()) {
            return bearersManager.genBoth(iduser, user.get().getRoles());

        } else {
            throw new NotSuchUserException();
        }

    }

    @Override
    public void clear() {
        users.clear();
        fill();
    }

    @Override
    public void signOut(long iduser, String password) throws BadPasswordException, NotSuchUserException {
        Optional<StoreUser> user = users.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (Arrays.equals(usr.getPassword(), PasswordEncoder.encode(password)))
                users.deleteById(iduser);
            else throw new BadPasswordException();

        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public StoreUser updateRoles(long iduser, Set<String> newRoles) throws NotSuchUserException {
        Optional<StoreUser> user = users.findById(iduser);
        if (user.isPresent()) {
            newRoles = newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());

            var usr = user.get();
            usr.setUpdateDate(LocalDateTime.now());
            usr.setRoles(newRoles);
            users.update(usr);
            return usr;
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updatePassword(long iduser, String oldPasssword, String newpasssword) throws NotSuchUserException, BadPasswordException, BadPasswordFormat {
        Optional<StoreUser> user = users.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (!passwordChecker.test(newpasssword)) throw new BadPasswordFormat();
            if (Arrays.equals(usr.getPassword(), PasswordEncoder.encode(oldPasssword))) {
                usr.setUpdateDate(LocalDateTime.now());
                usr.setPassword(PasswordEncoder.encode(newpasssword));
                users.update(usr);
            } else throw new BadPasswordException();
        } else {
            throw new NotSuchUserException();
        }
    }

    @Override
    public void updateMail(long iduser, String password, String newmail) throws MailAlreadyTakenException, NotSuchUserException, InvalidMail, BadPasswordException {
        Optional<StoreUser> user = users.findById(iduser);
        if (user.isPresent()) {
            var usr = user.get();
            if (!Arrays.equals(usr.getPassword(), PasswordEncoder.encode(password)))
                throw new BadPasswordException();
            if (!mailChecker.test(newmail))
                throw new InvalidMail();
            if (users.findByMail(newmail).isPresent())
                throw new MailAlreadyTakenException();

            usr.setMail(newmail);
            usr.setUpdateDate(LocalDateTime.now());
            users.update(usr);

        } else {
            throw new NotSuchUserException();
        }
    }
}
