package com.spo.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ValueNotAcceptedException extends ResponseStatusException {

    public ValueNotAcceptedException(final String message) {
        super(HttpStatus.PRECONDITION_FAILED, message);
    }
}
