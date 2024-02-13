package com.example.app.controllers;

import com.example.app.dto.request.cahsbox.CreateCashboxRequest;
import com.example.app.dto.response.cashbox.CashboxResponse;
import com.example.app.exceptions.BadRequestException;
import com.example.app.services.CashboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/cashbox")
@RequiredArgsConstructor
public class CashboxController {

    private final CashboxService cashboxService;

    @GetMapping("/create")
    public String getCreateCashboxPage(Model model) {
        model.addAttribute("cashbox", new CreateCashboxRequest());
        return "create_cashbox_page";
    }

    @PostMapping
    public String saveCashbox(
            @ModelAttribute("cashbox") CreateCashboxRequest request,
            Model model) {
        try {
            cashboxService.saveCashbox(request);
            return "redirect:/api/v1/cashbox";
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "error_page";
        }

    }

    @GetMapping
    public String getAllCashbox(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<CashboxResponse> allCashbox = cashboxService.getAllCashbox(page);

        model.addAttribute("cashbox", allCashbox.getContent());
        model.addAttribute("totalPages", allCashbox.getTotalPages());
        model.addAttribute("currentPage", page);
        return "cashbox_page";
    }

    @GetMapping("/{id}")
    public String getCashboxById(
            @PathVariable("id") Long cashboxId,
            Model model) {
        model.addAttribute("cashbox", cashboxService.getCashboxById(cashboxId));
        model.addAttribute("id", cashboxId);
        return "cashbox_detail_page";

    }

    @GetMapping("/delete/{id}")
    public String deleteCashboxById(@PathVariable("id") Long cashboxId) {
        cashboxService.deleteCashbox(cashboxId);
        return "redirect:/api/v1/cashbox";
    }
}
