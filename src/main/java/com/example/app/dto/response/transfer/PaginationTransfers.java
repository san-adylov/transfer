package com.example.app.dto.response.transfer;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationTransfers(
        List<TransfersResponse> transfers,
        int page,
        int size
) {
}
