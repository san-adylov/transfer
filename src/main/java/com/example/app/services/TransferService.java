package com.example.app.services;

import com.example.app.dto.request.transfer.CreateTransferRequest;
import com.example.app.dto.request.transfer.UpdateTransferRequest;
import com.example.app.dto.response.transfer.TransferResponse;
import com.example.app.dto.response.transfer.TransfersResponse;
import org.springframework.data.domain.Page;

public interface TransferService {

  void createTransfer(String username, CreateTransferRequest request);

  Page<TransfersResponse> getAllTransfers(String username, int page);

  TransferResponse getTransfer(String username, Long id);

  void updateTransfer(UpdateTransferRequest request, String userName);

  void deleteTransfer(Long id);


}
