package com.example.backend.controller;

import com.example.backend.entity.Class;
import com.example.backend.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    @Autowired
    private ClassRepository classRepo;

    @GetMapping
    public List<Class> list() {
        return classRepo.findAll();
    }

    @PostMapping
    public Class add(@RequestBody Class classEntity) {
        return classRepo.save(classEntity);  // id 由用户输入
    }

    @PutMapping("/{id}")
    public Class update(@PathVariable Long id, @RequestBody Class classEntity) {
        classEntity.setId(id);  // 使用传入的 id
        return classRepo.save(classEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        classRepo.deleteById(id);
    }
}
