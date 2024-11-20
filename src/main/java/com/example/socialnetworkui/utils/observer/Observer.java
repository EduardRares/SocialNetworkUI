package com.example.socialnetworkui.utils.observer;
import com.example.socialnetworkui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}