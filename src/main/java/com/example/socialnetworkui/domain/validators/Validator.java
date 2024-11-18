package com.example.socialnetworkui.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}