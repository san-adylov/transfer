package com.example.app.controllers;

import com.example.app.dto.request.transfer.CreateTransferRequest;
import com.example.app.dto.request.transfer.UpdateTransferRequest;
import com.example.app.dto.response.transfer.TransferResponse;
import com.example.app.dto.response.transfer.TransfersResponse;
import com.example.app.exceptions.BadRequestException;
import com.example.app.exceptions.ForbiddenException;
import com.example.app.exceptions.NotFoundException;
import com.example.app.services.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

  private final TransferService transferService;

  @GetMapping
  public String getAllTransfers(
      Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    Page<TransfersResponse> transferPage = transferService.getAllTransfers(username, page);
    model.addAttribute("transfers", transferPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", transferPage.getTotalPages());
    log.info("get all transfers controller");
    return "transfer_page";
  }

  @GetMapping("/create-transfer")
  public String createTransfer(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    try {
      String username = userDetails.getUsername();
      model.addAttribute("userLogin", username);
      model.addAttribute("request", new CreateTransferRequest());
      log.info("create transfer controller");
      return "create_transfer_page";
    } catch (NotFoundException | BadRequestException e) {
      model.addAttribute("error", e.getMessage());
      log.error("error %s".formatted(e.getMessage()));
    }
    return "redirect:/create-transfer";

  }

  @PostMapping("/save-transfer")
  public String saveTransfer(
      Model model,
      @ModelAttribute("request") CreateTransferRequest createTransferRequest,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      log.info("save transfer controller");
      transferService.createTransfer(userDetails.getUsername(), createTransferRequest);
      return "redirect:/api/v1/transfers";
    } catch (BadRequestException | NotFoundException e) {
      model.addAttribute("error", e.getMessage());
      log.info("error %s".formatted(e.getMessage()));
      return "create_transfer_page";
    }
  }

  @GetMapping("/edit-transfer")
  public String getUpdatePage(Model model) {
    model.addAttribute("update_transfer", new UpdateTransferRequest());
    log.info("update transfer controller");
    return "edit_transfer_page";
  }

  @PostMapping("/update-transfer")
  public String updateTransfer(
      Model model,
      @ModelAttribute("update_transfer") UpdateTransferRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      transferService.updateTransfer(request, userDetails.getUsername());
      log.info("save update transfer controller");
      return "redirect:/api/v1/transfers";
    } catch (NotFoundException | ForbiddenException | BadRequestException e) {
      model.addAttribute("errorMessage", e.getMessage());
      System.out.printf("error %s".formatted(e.getMessage()));
      log.error("error %s".formatted(e.getMessage()));
      return "edit_transfer_page";
    }
  }

  @GetMapping("/{id}")
  public String getTransferById(
      Model model,
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      TransferResponse transfer = transferService.getTransfer(userDetails.getUsername(), id);
      model.addAttribute("transfer", transfer);
      log.info("get transfer by id controller");
      return "transfer_detail_page";
    } catch (BadRequestException | NotFoundException | ForbiddenException e) {
      model.addAttribute("error", e.getMessage());
      log.error("error %s".formatted(e.getMessage()));
      return "error_page";
    }
  }


  @DeleteMapping("/transfer/{id}")
  public String deleteTransferById(@PathVariable("id") Long transferId) {
    transferService.deleteTransfer(transferId);
    log.info("delete transfer by id");
    return "redirect:/transfers";
  }


}
