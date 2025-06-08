package com.example.backend.controller;

import com.example.backend.entity.TeacherDormitory;
import com.example.backend.repository.TeacherDormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacherDormitory")
public class TeacherDormitoryController {

    @Autowired
    private TeacherDormitoryRepository teacherDormitoryRepo;

    // 获取所有教师宿舍信息
    @GetMapping
    public List<TeacherDormitory> list() {
        return teacherDormitoryRepo.findAll();
    }

    // 添加新的教师宿舍信息
    @PostMapping
    public TeacherDormitory add(@RequestBody TeacherDormitory teacherDormitory) {
        return teacherDormitoryRepo.save(teacherDormitory);  // id 由用户输入
    }

    // 根据 ID 更新教师宿舍信息
    @PutMapping("/{id}")
    public TeacherDormitory update(@PathVariable Long id, @RequestBody TeacherDormitory teacherDormitory) {
        teacherDormitory.setId(id);  // 使用传入的 id
        return teacherDormitoryRepo.save(teacherDormitory);
    }

    // 根据 ID 删除教师宿舍信息
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teacherDormitoryRepo.deleteById(id);
    }
}
