package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.repository.*;
import com.example.socialnetworkui.repository.db.UserDBRepository;

import java.util.NoSuchElementException;
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
        return userRepository.save(user);
    }

    public Optional<User> deleteUser(Long id) throws ValidationException {
        Optional<User> user = userRepository.findOne(id);
        if(user.isEmpty()) throw new ValidationException("Nu exista userul cu id-ul dat");
        Optional<User> temp = userRepository.delete(user.get().getId());
        for(User u : userRepository.findAll()) {
            if(u.getFriends().contains(user.get())) {
                u.removeFriend(user.get());
                userRepository.update(u);
            }
        }
        return temp;
    }

    public User updateUser(User user) throws ValidationException {
        Optional<User> temp = userRepository.findOne(user.getId());
        user.setId(temp.get().getId());
        userRepository.update(user);
        return user;
    }

    public User login(String email, String password) {
        return UserDBRepository.login(email, password);
    }

    public Optional<User> userById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}
