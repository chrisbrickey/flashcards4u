package com.flashcards4u.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CsvParsingException extends RuntimeException {

    public CsvParsingException(String message) {
        super(message);
    }
}
