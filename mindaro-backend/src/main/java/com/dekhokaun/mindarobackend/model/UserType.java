package com.dekhokaun.mindarobackend.model;

import lombok.Getter;

@Getter
public enum UserType {
    ADMIN("A"),
    MENTOR("M"),
    CUSTOMER("C");

    private final String code;

    UserType(String code) {
        this.code = code;
    }
}
