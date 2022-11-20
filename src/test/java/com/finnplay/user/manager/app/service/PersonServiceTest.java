package com.finnplay.user.manager.app.service;

import com.finnplay.user.manager.app.data.Person;
import com.finnplay.user.manager.app.dto.PersonEditDTO;
import com.finnplay.user.manager.app.exception.DuplicatePersonException;
import com.finnplay.user.manager.app.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    private static final String PERSON_EMAIL = "person email";
    private static final String PASSWORD_HASH = "password hash";
    private static final String PERSON_PASSWORD = "person password";
    private static final int PERSON_ID = 5675;
    private static final LocalDate PERSON_BIRTHDAY = LocalDate.ofEpochDay(23423434);
    private static final int PERSON_VERSION = 6756;
    private static final String PERSON_FIRST_NAME = "first name";
    private static final String PERSON_LAST_NAME = "last name";
    private static final String UPDATED = "Updated ";
    private static final int UPDATED_NUMBER = 277;
    private static final String PERSON_PLAIN_TEXT_PASSWORD = "password";

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final Person person = new Person();

    @InjectMocks
    private PersonService testedInstance;

    @BeforeEach
    void setUp() {
        person.setId(PERSON_ID);
        person.setBirthday(PERSON_BIRTHDAY);
        person.setVersion(PERSON_VERSION);
        person.setFirstName(PERSON_FIRST_NAME);
        person.setLastName(PERSON_LAST_NAME);
        person.setPasswordHash(PASSWORD_HASH);
        person.setEmail(PERSON_EMAIL);
    }

    @Test
    void testLoginInvalidData() {
        PersonEditDTO loggedPerson = testedInstance.login("invalid email", "invalid password");
        assertThat(loggedPerson, is(nullValue()));

        when(personRepository.findByEmail(PERSON_EMAIL)).thenReturn(person);
        loggedPerson = testedInstance.login(PERSON_EMAIL, "invalid password");
        assertThat(loggedPerson, is(nullValue()));
    }

    @Test
    void testLoginValidData() {
        when(personRepository.findByEmail(PERSON_EMAIL)).thenReturn(person);
        when(passwordEncoder.matches(PERSON_PASSWORD, PASSWORD_HASH)).thenReturn(true);

        PersonEditDTO loggedPerson = testedInstance.login(PERSON_EMAIL, PERSON_PASSWORD);

        assertThat(loggedPerson, is(notNullValue()));

        assertThat(loggedPerson.getPassword(), is(nullValue()));
        assertThat(loggedPerson.getEmail(), is(PERSON_EMAIL));
        assertThat(loggedPerson.getId(), is(PERSON_ID));
        assertThat(loggedPerson.getFirstName(), is(PERSON_FIRST_NAME));
        assertThat(loggedPerson.getLastName(), is(PERSON_LAST_NAME));
        assertThat(loggedPerson.getBirthday(), is(PERSON_BIRTHDAY));
        assertThat(loggedPerson.getVersion(), is(PERSON_VERSION));
    }

    @Test
    void testUpdateValidNoExistingEmail() {
        when(personRepository.getExistingUserId(PERSON_EMAIL)).thenReturn(null);
        checkCorrectUserUpdate();
    }

    @Test
    void testUpdateValidSameExistingEmail() {
        when(personRepository.getExistingUserId(PERSON_EMAIL)).thenReturn(PERSON_ID);
        checkCorrectUserUpdate();
    }

    @Test()
    void testUpdateExistingDuplicateEmail() {
        when(personRepository.getExistingUserId(PERSON_EMAIL)).thenReturn(234);

        PersonEditDTO sourcePerson = new PersonEditDTO();

        sourcePerson.setId(PERSON_ID);
        sourcePerson.setBirthday(PERSON_BIRTHDAY);
        sourcePerson.setVersion(PERSON_VERSION);
        sourcePerson.setFirstName(PERSON_FIRST_NAME);
        sourcePerson.setLastName(PERSON_LAST_NAME);
        sourcePerson.setPassword(PERSON_PLAIN_TEXT_PASSWORD);
        sourcePerson.setEmail(PERSON_EMAIL);

        assertThrows(DuplicatePersonException.class, () -> testedInstance.update(sourcePerson));
    }

    @Test()
    void testUpdateNewDuplicateEmail() {
        when(personRepository.getExistingUserId(PERSON_EMAIL)).thenReturn(234);

        PersonEditDTO sourcePerson = new PersonEditDTO();

        sourcePerson.setBirthday(PERSON_BIRTHDAY);
        sourcePerson.setVersion(PERSON_VERSION);
        sourcePerson.setFirstName(PERSON_FIRST_NAME);
        sourcePerson.setLastName(PERSON_LAST_NAME);
        sourcePerson.setPassword(PERSON_PLAIN_TEXT_PASSWORD);
        sourcePerson.setEmail(PERSON_EMAIL);

        assertThrows(DuplicatePersonException.class, () -> testedInstance.update(sourcePerson));
    }

    private void checkCorrectUserUpdate() {
        when(personRepository.save(any())).then((Answer<Person>) invocation -> {
            Person original = invocation.getArgument(0);

            Person saved = new Person();

            saved.setEmail(UPDATED + original.getEmail());
            saved.setId(UPDATED_NUMBER + original.getId());
            saved.setBirthday(LocalDate.ofEpochDay(original.getBirthday().toEpochDay() + UPDATED_NUMBER));
            saved.setFirstName(UPDATED + original.getFirstName());
            saved.setLastName(UPDATED + original.getLastName());
            saved.setVersion(UPDATED_NUMBER + original.getVersion());
            saved.setPasswordHash(UPDATED + original.getPasswordHash());

            return saved;
        });

        PersonEditDTO sourcePerson = new PersonEditDTO();

        sourcePerson.setId(PERSON_ID);
        sourcePerson.setBirthday(PERSON_BIRTHDAY);
        sourcePerson.setVersion(PERSON_VERSION);
        sourcePerson.setFirstName(PERSON_FIRST_NAME);
        sourcePerson.setLastName(PERSON_LAST_NAME);
        sourcePerson.setPassword(PERSON_PLAIN_TEXT_PASSWORD);
        sourcePerson.setEmail(PERSON_EMAIL);

        PersonEditDTO updatedPerson = testedInstance.update(sourcePerson);
        assertThat(updatedPerson, is(notNullValue()));

        assertThat(updatedPerson.getPassword(), is(PERSON_PLAIN_TEXT_PASSWORD));
        assertThat(updatedPerson.getEmail(), is(UPDATED + PERSON_EMAIL));
        assertThat(updatedPerson.getId(), is(UPDATED_NUMBER + PERSON_ID));
        assertThat(updatedPerson.getFirstName(), is(UPDATED + PERSON_FIRST_NAME));
        assertThat(updatedPerson.getLastName(), is(UPDATED + PERSON_LAST_NAME));
        assertThat(updatedPerson.getBirthday(), is(LocalDate.ofEpochDay(UPDATED_NUMBER + PERSON_BIRTHDAY.toEpochDay())));
        assertThat(updatedPerson.getVersion(), is(UPDATED_NUMBER + PERSON_VERSION));
    }
}