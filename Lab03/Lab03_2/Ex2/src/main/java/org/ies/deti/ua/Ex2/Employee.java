package org.ies.deti.ua.Ex2;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
   @Column(name="name",nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee (id: " +this.getId() + "): name = " + this.getName() + " email = " + this.getEmail();
    }
}
