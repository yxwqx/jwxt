package com.example.backend.controller;

import com.example.backend.entity.Student;
import com.example.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping
    public List<Student> list() {
        return studentRepo.findAll();
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return studentRepo.save(student);  // id 由用户输入
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);  // 使用传入的 id
        return studentRepo.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentRepo.deleteById(id);
    }
}
