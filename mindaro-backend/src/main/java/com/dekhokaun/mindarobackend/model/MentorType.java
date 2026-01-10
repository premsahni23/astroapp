package com.dekhokaun.mindarobackend.model;

import lombok.Getter;

@Getter
public enum MentorType {
    BASIC("B"),
    PREMIUM("P"),
    ELITE("E");

    private final String code;

    MentorType(String code) {
        this.code = code;
    }

}
