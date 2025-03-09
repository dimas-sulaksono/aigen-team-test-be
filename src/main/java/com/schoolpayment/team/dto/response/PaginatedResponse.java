package com.schoolpayment.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private int status;
    private List<T> data;
    private int totalPages;
    private int currentPage;
    private int size;
    private long totalElements;

    public PaginatedResponse(int status, Page<T> page) {
        this.status = status;
        this.data = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
    }

}

