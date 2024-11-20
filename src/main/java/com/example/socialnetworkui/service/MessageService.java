package com.example.socialnetworkui.service;

import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.repository.Repository;
import com.example.socialnetworkui.repository.db.MessageDBRepository;
import com.example.socialnetworkui.utils.events.ChangeEventType;
import com.example.socialnetworkui.utils.events.EntityChangeEvent;
import com.example.socialnetworkui.utils.observer.Observable;
import com.example.socialnetworkui.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageService implements Observable<EntityChangeEvent> {
    private final Repository<Long, Message> repo;
    private List<Observer<EntityChangeEvent>> observers=new ArrayList<>();

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

    public Iterable<Message> findAllbyUser(Long id) {
        return MessageDBRepository.findAllbyUser(id);
    }

    public void markRead(Long from, Long to) {
        MessageDBRepository.markRead(from, to);
    }

    public Iterable<Long> noofMessages(Long id){
        return MessageDBRepository.noofUnreadMessages(id);
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
