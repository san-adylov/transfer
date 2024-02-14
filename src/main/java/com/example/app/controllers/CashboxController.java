package com.example.app.controllers;

import com.example.app.dto.request.cahsbox.CreateCashboxRequest;
import com.example.app.dto.response.cashbox.CashboxResponse;
import com.example.app.exceptions.BadRequestException;
import com.example.app.services.CashboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/v1/cashbox")
@RequiredArgsConstructor
public class CashboxController {

  private final CashboxService cashboxService;

  @GetMapping("/create")
  public String getCreateCashboxPage(Model model) {
    model.addAttribute("cashbox", new CreateCashboxRequest());
    log.info("get create cashbox page controller");
    return "create_cashbox_page";
  }

  @PostMapping("/save")
  public String saveCashbox(
      @ModelAttribute("cashbox") CreateCashboxRequest request,
      Model model) {
    try {
      cashboxService.saveCashbox(request);
      log.info("save cashbox controller");
      return "redirect:/api/v1/cashbox";
    } catch (BadRequestException e) {
      log.error("error save cashbox controller %s".formatted(e.getMessage()));
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
    log.info("get all cashbox controller");
    return "cashbox_page";
  }

  @GetMapping("/{id}")
  public String getCashboxById(
      @PathVariable("id") Long cashboxId,
      Model model) {
    model.addAttribute("cashbox", cashboxService.getCashboxById(cashboxId));
    model.addAttribute("id", cashboxId);
    log.info("get cashbox by id controller");
    return "cashbox_detail_page";

  }

  @GetMapping("/delete/{id}")
  public String deleteCashboxById(@PathVariable("id") Long cashboxId) {
    cashboxService.deleteCashboxById(cashboxId);
    log.info("delete cashbox by id controller");
    return "redirect:/api/v1/cashbox";
  }
}
