package ru.yandex.intershop.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.intershop.error.exception.ConflictOnRequestException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public String handleConflictException(final ConflictOnRequestException e, Model model) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage());
        model.addAttribute("errorResponse", errorResponse);
        return "error";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public String handleAnyOtherException(final Exception e, Model model) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage());
        model.addAttribute("errorResponse", errorResponse);
        return "error";
    }
}
