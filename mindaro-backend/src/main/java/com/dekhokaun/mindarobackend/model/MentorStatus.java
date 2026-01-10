package com.dekhokaun.mindarobackend.model;

public enum MentorStatus {
    ACTIVE("A"),
    INACTIVE("I"),
    SUSPENDED("S"),
    PENDING_APPROVAL("P");

    private final String code;

    MentorStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
