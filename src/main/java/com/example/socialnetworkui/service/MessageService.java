package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.repository.Repository;
import com.example.socialnetworkui.repository.db.MessageDBRepository;

import java.util.Optional;

public class MessageService {
    private final Repository<Long, Message> repo;
    public MessageService(Repository<Long, Message> repository) {
        this.repo = repository;
    }

    public Optional<Message> addMessage(Message message) {
        return repo.save(message);
    }

    public Optional<Message> deleteMessage(Long id) throws ValidationException {
        Optional<Message> message = repo.findOne(id);
        if(message.isEmpty()) throw new ValidationException("Nu exista mesajul cu id-ul dat");
        return repo.delete(message.get().getId());
    }
}
