package com.dekhokaun.mindarobackend.payload.request;

import com.dekhokaun.mindarobackend.model.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingStatusUpdateRequest {
    @NotNull
    private BookingStatus status;
}
