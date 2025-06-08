package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class Class {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;  // 由用户输入

    private String name;
    private String department;

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
