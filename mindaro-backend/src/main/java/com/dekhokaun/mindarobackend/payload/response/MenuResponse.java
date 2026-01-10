package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class MenuResponse {

    private String text;
    private String icon;
    private String action;
    private String link;
    private int order;
}
