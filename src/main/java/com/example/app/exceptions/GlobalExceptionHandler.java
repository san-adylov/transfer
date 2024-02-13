package com.example.app.exceptions;

import lombok.Builder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GetMapping
    @ExceptionHandler({NotFoundException.class})
    public String notFoundExceptionHandler(NotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error_page";
    }

    @GetMapping
    @ExceptionHandler({BadRequestException.class})
    public String badRequestHandler(BadRequestException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error_page";
    }
    @GetMapping
    @ExceptionHandler({ForbiddenException.class})
    public String forbiddenHandler(ForbiddenException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error_page";
    }
    @GetMapping
    @ExceptionHandler({NullPointerException.class})
    public String forbiddenHandler(NullPointerException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error_page";
    }
    @GetMapping
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException e, Model model) {
        List<String> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        model.addAttribute("errorMessage", errors);
        return "error_page";
    }
}
