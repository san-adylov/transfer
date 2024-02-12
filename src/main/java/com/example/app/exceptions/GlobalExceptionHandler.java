package com.example.app.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GetMapping
    @ExceptionHandler({NotFoundException.class})
    public String notFoundExceptionHandler(NotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @GetMapping
    @ExceptionHandler({BadRequestException.class})
    public String badRequestHandler(BadRequestException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
    @GetMapping
    @ExceptionHandler({ForbiddenException.class})
    public String forbiddenHandler(ForbiddenException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
    @GetMapping
    @ExceptionHandler({NullPointerException.class})
    public String forbiddenHandler(NullPointerException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
