package ru.yandex.intershop.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    String error;
    String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

}
