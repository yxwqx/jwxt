package com.example.backend.controller;

import com.example.backend.entity.Student;
import com.example.backend.entity.Teacher;
import com.example.backend.entity.Course;
import com.example.backend.entity.Class;
import com.example.backend.repository.StudentRepository;
import com.example.backend.repository.TeacherRepository;
import com.example.backend.repository.CourseRepository;
import com.example.backend.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneralController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClassRepository classRepository;

    @GetMapping("/{entity}")
    public List<?> getEntityData(@PathVariable String entity) {
        switch (entity) {
            case "student":
                return studentRepository.findAll();
            case "teacher":
                return teacherRepository.findAll();
            case "course":
                return courseRepository.findAll();
            case "class":
                return classRepository.findAll();
            default:
                return null;
        }
    }

    @PostMapping("/{entity}")
    public Object addEntity(@PathVariable String entity, @RequestBody Object entityObject) {
        switch (entity) {
            case "student":
                return studentRepository.save((Student) entityObject);
            case "teacher":
                return teacherRepository.save((Teacher) entityObject);
            case "course":
                return courseRepository.save((Course) entityObject);
            case "class":
                return classRepository.save((Class) entityObject);
            default:
                return null;
        }
    }

    @PutMapping("/{entity}/{id}")
    public Object updateEntity(@PathVariable String entity, @PathVariable Long id, @RequestBody Object entityObject) {
        switch (entity) {
            case "student":
                ((Student) entityObject).setId(id);
                return studentRepository.save((Student) entityObject);
            case "teacher":
                ((Teacher) entityObject).setId(id);
                return teacherRepository.save((Teacher) entityObject);
            case "course":
                ((Course) entityObject).setId(id);
                return courseRepository.save((Course) entityObject);
            case "class":
                ((Class) entityObject).setId(id);
                return classRepository.save((Class) entityObject);
            default:
                return null;
        }
    }

    @DeleteMapping("/{entity}/{id}")
    public void deleteEntity(@PathVariable String entity, @PathVariable Long id) {
        switch (entity) {
            case "student":
                studentRepository.deleteById(id);
                break;
            case "teacher":
                teacherRepository.deleteById(id);
                break;
            case "course":
                courseRepository.deleteById(id);
                break;
            case "class":
                classRepository.deleteById(id);
                break;
            default:
                break;
        }
    }
}
