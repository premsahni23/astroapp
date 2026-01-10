package com.dekhokaun.mindarobackend.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class NotifyCallRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String creatorId;

    @NotBlank
    private String userName;

    @NotBlank
    private String roomId;

    @NotNull
    private CallType callType;

    @Getter
    public enum CallType {
        VIDEO_CALL("video"),
        VOICE_CALL("voice");

        private final String value;

        CallType(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CallType fromValue(String value) {
            for (CallType type : CallType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid callType: " + value);
        }
    }
}
