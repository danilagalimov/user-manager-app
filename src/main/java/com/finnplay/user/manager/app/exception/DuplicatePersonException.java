package com.finnplay.user.manager.app.exception;

public class DuplicatePersonException extends Exception {
    public DuplicatePersonException(String errorMessage) {
        super(errorMessage);
    }
}
