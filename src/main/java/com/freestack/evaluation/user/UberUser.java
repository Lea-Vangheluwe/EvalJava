package com.freestack.evaluation.user;

import javax.persistence.*;

@Entity
@Table (name="uber_user")
public class UberUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;

    public UberUser(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UberUser() {
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
