package com.finnplay.user.manager.app.service;

import com.finnplay.user.manager.app.data.Person;
import com.finnplay.user.manager.app.data.PersonIdOnly;
import com.finnplay.user.manager.app.dto.PersonEditRequest;
import com.finnplay.user.manager.app.exception.DuplicatePersonException;
import com.finnplay.user.manager.app.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PersonEditRequest login(String email, String password) {
        Person person = personRepository.findByEmail(email, Person.class);

        if (null != person && passwordEncoder.matches(password, person.getPasswordHash())) {
            return updatePersonDTOFromPerson(new PersonEditRequest(), person);
        }

        return null;
    }

    @Transactional
    public PersonEditRequest update(PersonEditRequest personDTO) throws DuplicatePersonException {
        Person person = assemblePersonFromDTO(personDTO);

        PersonIdOnly existingPersonId = personRepository.findByEmail(person.getEmail(), PersonIdOnly.class);

        if (null != existingPersonId &&
                (null == person.getId() || !existingPersonId.getId().equals(person.getId()))) {
            throw new DuplicatePersonException("Person with this email already exists");
        }

        person = personRepository.save(person);

        return updatePersonDTOFromPerson(personDTO, person);
    }

    private static PersonEditRequest updatePersonDTOFromPerson(PersonEditRequest personDTO, Person person) {
        personDTO.setId(person.getId());
        personDTO.setBirthday(person.getBirthday());
        personDTO.setEmail(person.getEmail());
        personDTO.setVersion(person.getVersion());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());

        return personDTO;
    }

    private Person assemblePersonFromDTO(PersonEditRequest personDTO)  {
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
