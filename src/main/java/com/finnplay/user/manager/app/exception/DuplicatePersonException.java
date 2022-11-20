package com.finnplay.user.manager.app.exception;

public class DuplicatePersonException extends RuntimeException {
    public DuplicatePersonException(String errorMessage) {
        super(errorMessage);
    }
}
