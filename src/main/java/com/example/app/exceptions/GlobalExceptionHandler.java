package com.example.app.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({NotFoundException.class})
//    public String notFoundExceptionHandler(NotFoundException e, Model model) {
//        model.addAttribute("errorMessage", e.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler({BadRequestException.class})
//    public String badRequestHandler(BadRequestException e, Model model) {
//        model.addAttribute("errorMessage", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler({ForbiddenException.class})
//    public String forbiddenHandler(ForbiddenException e, Model model) {
//        model.addAttribute("errorMessage", e.getMessage());
//        return "error";
//    }
}
