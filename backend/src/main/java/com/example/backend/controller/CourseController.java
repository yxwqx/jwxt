package com.example.backend.controller;

import com.example.backend.entity.Course;
import com.example.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepo;

    @GetMapping
    public List<Course> list() {
        return courseRepo.findAll();
    }

    @PostMapping
    public Course add(@RequestBody Course course) {
        return courseRepo.save(course);  // id 由用户输入
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);  // 使用传入的 id
        return courseRepo.save(course);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseRepo.deleteById(id);
    }
}
