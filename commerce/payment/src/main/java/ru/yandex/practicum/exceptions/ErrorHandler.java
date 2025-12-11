package ru.yandex.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler extends BaseErrorHandler {

    @Override
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoOrderFound(NoOrderFoundException e) {
        log.error("No order found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotEnoughInfoInOrderToCalculate(NotEnoughInfoInOrderToCalculateException e) {
        log.error("Not enough info in order to calculate {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }
}