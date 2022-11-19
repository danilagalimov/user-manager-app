package com.finnplay.user.manager.app.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(length = 254)
    private String email;

    @Column(length = 35)
    private String firstName;

    @Column(length = 35)
    private String lastName;

    @Column
    private Date birthday;

    @Column(length = 60)
    private String passwordHash;

    @Version
    @Column
    private Integer version;
}
