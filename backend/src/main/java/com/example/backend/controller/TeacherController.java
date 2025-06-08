package com.example.backend.controller;

import com.example.backend.entity.Teacher;
import com.example.backend.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepo;

    @GetMapping
    public List<Teacher> list() {
        return teacherRepo.findAll();
    }

    @PostMapping
    public Teacher add(@RequestBody Teacher teacher) {
        return teacherRepo.save(teacher);  // id 由用户输入
    }

    @PutMapping("/{id}")
    public Teacher update(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);  // 使用传入的 id
        return teacherRepo.save(teacher);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teacherRepo.deleteById(id);
    }
}
