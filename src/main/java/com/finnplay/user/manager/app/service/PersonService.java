package com.finnplay.user.manager.app.service;

import com.finnplay.user.manager.app.data.Person;
import com.finnplay.user.manager.app.dto.PersonEditDTO;
import com.finnplay.user.manager.app.repository.PersonRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public PersonEditDTO login(String email, String password) throws ReflectiveOperationException {
        Person person = personRepository.findByEmail(email);

        if (null != person && passwordEncoder.matches(password, person.getPasswordHash())) {
            return updatePersonDTOFromPerson(new PersonEditDTO(), person);
        }

        return null;
    }

    @Transactional
    public PersonEditDTO update(PersonEditDTO personDTO) throws ReflectiveOperationException {
        Person person = assemblePersonFromDTO(personDTO);

        person = personRepository.save(person);

        return updatePersonDTOFromPerson(personDTO, person);
    }

    private static PersonEditDTO updatePersonDTOFromPerson(PersonEditDTO personDTO, Person person) throws ReflectiveOperationException {
        PropertyUtils.copyProperties(personDTO, person);

        return personDTO;
    }

    private Person assemblePersonFromDTO(PersonEditDTO personDTO) throws ReflectiveOperationException {
        Person person = new Person();

        PropertyUtils.copyProperties(person, personDTO);
        person.setPasswordHash(passwordEncoder.encode(personDTO.getPassword()));

        return person;
    }
}
