package ru.yandex.intershop.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IncorrectRequestException extends RuntimeException {

    public IncorrectRequestException(String message) {
        super(message);

    }

}
