package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;

    public User create(User user) {
        checkEmailNotUsed(user.getEmail());
        checkPhoneNumberNotUsed(user.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User update(UUID id, User user) {
        securityService.authorize(id);
        User existingUser = getById(id);

        if (!user.getEmail().equals(existingUser.getEmail())) {
            checkEmailNotUsed(user.getEmail());
        }
        if (!user.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            checkPhoneNumberNotUsed(user.getPhoneNumber());
        }

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(existingUser);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "user", id));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void deleteById(UUID id) {
        securityService.authorize(id);
        userRepository.delete(getById(id));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private void checkEmailNotUsed(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ExceptionFactory().createException(HttpStatus.CONFLICT,
                    ErrorMessage.ALREADY_EXISTS, "A user with this email");
        }
    }

    private void checkPhoneNumberNotUsed(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ExceptionFactory().createException(HttpStatus.CONFLICT,
                    ErrorMessage.ALREADY_EXISTS, "A user with this phone number");
        }
    }
}
