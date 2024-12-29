package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.repository.*;
import com.example.socialnetworkui.repository.db.UserDBRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

public class UserService {
    private final Repository<Long, User> userRepository;

    public UserService(Repository<Long, User> repository) {
        this.userRepository = repository;
    }

    public Optional<User> addUser(User user) throws ValidationException {
        for(User u : userRepository.findAll()) {
            if(u.equals(user)) throw new ValidationException("Exista deja userul introdus");
        }
        Long idMax = 0L;
        for(User u : userRepository.findAll()) {
            if(idMax < u.getId()) {
                idMax = u.getId();
            }
        }
        user.setId(idMax + 1);
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> deleteUser(Long id) throws ValidationException {
        Optional<User> user = userRepository.findOne(id);
        if(user.isEmpty()) throw new ValidationException("Nu exista userul cu id-ul dat");
        return userRepository.delete(user.get().getId());
    }

    public User updateUser(User user) throws ValidationException {
        Optional<User> temp = userRepository.findOne(user.getId());
        user.setId(temp.get().getId());
        userRepository.update(user);
        return user;
    }

    public User login(String email, String password) {
        if("admin".equals(password)) return UserDBRepository.login(email, password);
        return UserDBRepository.login(email, hashPassword(password));
    }

    public Optional<User> userById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    private String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updatetoHashPassword() {
        for(User u : userRepository.findAll()) {
            u.setPassword(hashPassword(u.getPassword()));
            userRepository.update(u);
        }
    }
}
