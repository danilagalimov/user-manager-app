package com.finnplay.user.manager.app.controller;

import com.finnplay.user.manager.app.dto.PersonEditDTO;
import com.finnplay.user.manager.app.dto.PersonLoginDTO;
import com.finnplay.user.manager.app.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
@SessionAttributes({PersonEditDTO.PERSON_EDIT_BEAN_NAME, PersonLoginDTO.PERSON_LOGIN_BEAN_NAME})
@Slf4j
public class PersonController {
    private static final String LOGIN_PAGE_TEMPLATE = "login/LoginPage";
    private static final String EDIT_PAGE_TEMPLATE = "login/EditPersonPage";
    private static final String LOGIN_PAGE_URL = "/login";
    private static final String EDIT_PAGE_URL = "/edit";
    private static final String LOGOUT_URL = "/logout";
    private static final String REDIRECT_PREFIX = "redirect:";

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    public RedirectView displayHomePage() {
        return new RedirectView(LOGIN_PAGE_URL);
    }


    @ModelAttribute(PersonLoginDTO.PERSON_LOGIN_BEAN_NAME)
    @Lookup
    public PersonLoginDTO personLogin() {
        return null;
    }

    @ModelAttribute(PersonEditDTO.PERSON_EDIT_BEAN_NAME)
    @Lookup
    public PersonEditDTO personEdit() {
        return null;
    }

    @GetMapping(LOGIN_PAGE_URL)
    public String doLogin() {
        return LOGIN_PAGE_TEMPLATE;
    }

    @PostMapping(LOGIN_PAGE_URL)
    public String doLogin(@ModelAttribute(PersonLoginDTO.PERSON_LOGIN_BEAN_NAME) @Valid PersonLoginDTO personLoginDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return LOGIN_PAGE_TEMPLATE;
        }

        try {
            PersonEditDTO loggedPerson = personService.login(personLoginDTO.getEmail(), personLoginDTO.getPassword());
            if (null != loggedPerson) {
                updatePersonBean(model, loggedPerson);

                return REDIRECT_PREFIX + LOGIN_PAGE_URL;
            } else {
                addError(result, "Invalid username or password");
                return LOGIN_PAGE_TEMPLATE;
            }
        } catch (Exception e) {
            log.error("Failed to log in", e);
            addError(result, e.getMessage());

            return LOGIN_PAGE_TEMPLATE;
        }
    }

    @GetMapping(EDIT_PAGE_URL)
    public String doEdit() {
        return EDIT_PAGE_TEMPLATE;
    }

    @PostMapping(EDIT_PAGE_URL)
    public String doEdit(@ModelAttribute(PersonEditDTO.PERSON_EDIT_BEAN_NAME) @Valid PersonEditDTO personDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return EDIT_PAGE_TEMPLATE;
        }

        try {
            updatePersonBean(model, personService.update(personDTO));
        } catch (Exception e) {
            log.error("Failed to edit user", e);
            addError(result, e.getMessage());
            return EDIT_PAGE_TEMPLATE;
        }

        return LOGIN_PAGE_TEMPLATE;
    }

    @GetMapping(LOGOUT_URL)
    public String doLogout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();

        return REDIRECT_PREFIX + LOGIN_PAGE_URL;
    }

    private static void addError(BindingResult result, String errorMessage) {
        result.addError(new ObjectError("globalError", errorMessage));
    }

    private static void updatePersonBean(Model model, PersonEditDTO loggedPerson) {
        model.addAttribute(PersonEditDTO.PERSON_EDIT_BEAN_NAME, loggedPerson);
    }

}
