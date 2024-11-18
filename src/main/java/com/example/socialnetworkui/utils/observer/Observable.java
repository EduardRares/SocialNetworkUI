package com.example.socialnetworkui.utils.observer;


import com.example.socialnetworkui.domain.User;

public interface Observable {
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers(User t);
}
