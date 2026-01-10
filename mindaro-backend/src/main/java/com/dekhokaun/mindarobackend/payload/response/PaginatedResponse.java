package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Matches admin-dashboard contract:
 * { items: T[], page: number, size: number, total: number }
 */
@Data
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;
    /** 1-based page number */
    private int page;
    private int size;
    private long total;
}
