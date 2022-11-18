package com.finnplay.user.manager.app.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PersonLoginDTO {
    public static final String PERSON_LOGIN_BEAN_NAME = "personLogin";
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
