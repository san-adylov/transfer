package com.example.app.controllers;

import com.example.app.dto.request.transfer.CreateTransferRequest;
import com.example.app.dto.request.transfer.UpdateTransferRequest;
import com.example.app.dto.response.transfer.TransfersResponse;
import com.example.app.exceptions.BadRequestException;
import com.example.app.exceptions.ForbiddenException;
import com.example.app.exceptions.NotFoundException;
import com.example.app.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transfers")
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
        model.addAttribute("userLogin", username);
        model.addAttribute("transfers", transferPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transferPage.getTotalPages());
        return "transfer_page";
    }

    @GetMapping("/create-transfer")
    public String createTransfer(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        model.addAttribute("userLogin", username);
        model.addAttribute("request", new CreateTransferRequest());
        return "create_transfer";
    }

    @PostMapping("/save-transfer")
    public String saveTransfer(
            Model model,
            @ModelAttribute("request") CreateTransferRequest createTransferRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            transferService.createTransfer(userDetails.getUsername(), createTransferRequest);
        } catch (BadRequestException | NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "create_transfer";
        }
        return "redirect:/transfer";
    }

    @GetMapping("/edit-transfer")
    public String getUpdatePage(Model model) {
        model.addAttribute("update_transfer", new UpdateTransferRequest());
        return "edit_transfer_page";
    }

    @PostMapping("/update-transfer")
    public String updateTransfer(
            Model model,
            @ModelAttribute("update_transfer") UpdateTransferRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
        transferService.updateTransfer(request, userDetails.getUsername());
        }catch (NotFoundException | ForbiddenException | BadRequestException e){
            model.addAttribute("errorMessage", e.getMessage());
            System.out.println("Error " + e.getMessage());
            return "edit_transfer_page";
        }
        return "redirect:/transfer";
    }
    @GetMapping("/transfer/{id}")
    public String getTransferById(@PathVariable Long id){
        return
    }
}