package com.norwegiancorgi.fileserver.api.models;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
