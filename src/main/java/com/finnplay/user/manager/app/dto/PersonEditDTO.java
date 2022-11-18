package com.finnplay.user.manager.app.dto;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonEditDTO {
    public static final String PERSON_EDIT_BEAN_NAME = "personEdit";

    Integer id;

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthday;

    @NotEmpty
    String password;

    Integer version;
}
