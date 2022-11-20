package com.finnplay.user.manager.app.dto;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonEditDTO {
    Integer id;

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 2, max = 35)
    String firstName;

    @NotEmpty
    @Size(min = 2, max = 35)
    String lastName;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @NotEmpty
    @Size(min = 3, max = 72)
    String password;

    Integer version;
}
