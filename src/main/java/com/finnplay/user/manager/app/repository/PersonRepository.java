package com.finnplay.user.manager.app.repository;

import com.finnplay.user.manager.app.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    <T> T findByEmail(String email, Class<T> resultClass);
}
