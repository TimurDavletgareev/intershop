package ru.yandex.intershop.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.intershop.error.exception.ConflictOnRequestException;
import ru.yandex.intershop.error.exception.IncorrectRequestException;
import ru.yandex.intershop.error.exception.NotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public String  handleNotFoundException(final NotFoundException e, Model model) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage());
        model.addAttribute("errorResponse", errorResponse);
        return "error";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public String handleIncorrectException(final IncorrectRequestException e, Model model) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage());
        model.addAttribute("errorResponse", errorResponse);
        return "error";
    }

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
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage());
        model.addAttribute("errorResponse", errorResponse);
        return "error";
    }
}
