package com.finnplay.user.manager.app.service;

import com.finnplay.user.manager.app.data.Person;
import com.finnplay.user.manager.app.data.PersonIdOnly;
import com.finnplay.user.manager.app.dto.PersonEditDTO;
import com.finnplay.user.manager.app.exception.DuplicatePersonException;
import com.finnplay.user.manager.app.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PersonEditDTO login(String email, String password) {
        log.debug("Trying to find the user by email {}", email);
        Person person = personRepository.findByEmail(email, Person.class);

        if (null != person) {
            if (passwordEncoder.matches(password, person.getPasswordHash())) {
                log.debug("Successfully logged in");

                return updatePersonDTOFromPerson(new PersonEditDTO(), person);
            } else {
                log.debug("Entered password do not match");
            }
        }

        return null;
    }

    @Transactional
    public PersonEditDTO update(PersonEditDTO personDTO) throws DuplicatePersonException {
        Person person = assemblePersonFromDTO(personDTO);

        PersonIdOnly existingPersonId = personRepository.findByEmail(person.getEmail(), PersonIdOnly.class);

        if (null != existingPersonId &&
                (null == person.getId() || !existingPersonId.getId().equals(person.getId()))) {
            log.debug("User {} tried to set email to existing (user with id {})", person.getId(), existingPersonId.getId());

            throw new DuplicatePersonException("Person with this email already exists");
        }

        person = personRepository.save(person);

        return updatePersonDTOFromPerson(personDTO, person);
    }

    private static PersonEditDTO updatePersonDTOFromPerson(PersonEditDTO personDTO, Person person) {
        personDTO.setId(person.getId());
        personDTO.setBirthday(person.getBirthday());
        personDTO.setEmail(person.getEmail());
        personDTO.setVersion(person.getVersion());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());

        return personDTO;
    }

    private Person assemblePersonFromDTO(PersonEditDTO personDTO)  {
        Person person = new Person();

        person.setId(personDTO.getId());
        person.setEmail(personDTO.getEmail());
        person.setBirthday(personDTO.getBirthday());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setVersion(personDTO.getVersion());

        person.setPasswordHash(passwordEncoder.encode(personDTO.getPassword()));

        return person;
    }
}
