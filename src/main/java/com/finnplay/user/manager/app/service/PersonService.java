package com.finnplay.user.manager.app.service;

import com.finnplay.user.manager.app.data.Person;
import com.finnplay.user.manager.app.dto.PersonEditDTO;
import com.finnplay.user.manager.app.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public PersonEditDTO login(String email, String password) {
        Person person = personRepository.findByEmail(email);

        if (null != person && passwordEncoder.matches(password, person.getPasswordHash())) {
            return updatePersonDTOFromPerson(new PersonEditDTO(), person);
        }

        return null;
    }

    @Transactional
    public PersonEditDTO update(PersonEditDTO personDTO) {
        Person person = assemblePersonFromDTO(personDTO);

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
