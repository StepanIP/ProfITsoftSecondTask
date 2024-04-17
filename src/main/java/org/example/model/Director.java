package org.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Director {
    @Id
    private int id;
    private String name;

    public Director(String name) {
        this.name = name;
    }

    public Director() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
