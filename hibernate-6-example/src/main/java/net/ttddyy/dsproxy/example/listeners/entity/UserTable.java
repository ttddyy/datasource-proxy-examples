package net.ttddyy.dsproxy.example.listeners.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class UserTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    @Column(nullable = false)
    private String name;

    public UserTable() {
    }

    public UserTable(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
