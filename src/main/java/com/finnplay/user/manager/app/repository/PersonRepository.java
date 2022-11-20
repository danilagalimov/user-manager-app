package com.finnplay.user.manager.app.repository;

import com.finnplay.user.manager.app.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    @Query("select entity.id from #{#entityName} entity where entity.email = :email")
    Integer getExistingUserId(@Param("email") String email);
}
