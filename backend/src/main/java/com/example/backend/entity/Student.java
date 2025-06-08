package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;  // 由用户输入

    private String name;
    private String gender;
    private Integer age;

    @Column(name = "class_name")
    private String className;

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
