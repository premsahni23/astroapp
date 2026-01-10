package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class SessionResponse {
    private String id;
//    private String userid;
    private String logintime;
    private String logouttime;
}
