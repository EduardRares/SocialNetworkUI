package com.example.socialnetworkui.utils;

public class Page<E> {
    private Iterable<E> elements;
    public Page(Iterable<E> elements) {
        this.elements = elements;
    }
    public Iterable<E> getElements() {
        return elements;
    }
}
