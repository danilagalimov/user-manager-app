package com.finnplay.user.manager.app.dto;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonLoginDTO {
    public static final String PERSON_LOGIN_BEAN_NAME = "personLogin";
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
