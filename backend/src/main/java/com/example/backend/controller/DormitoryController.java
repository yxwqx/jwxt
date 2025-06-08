package com.example.backend.controller;

import com.example.backend.entity.Dormitory;
import com.example.backend.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dormitory")
public class DormitoryController {

    @Autowired
    private DormitoryRepository dormitoryRepo;

    // 获取所有宿舍信息
    @GetMapping
    public List<Dormitory> list() {
        return dormitoryRepo.findAll();
    }

    // 添加新的宿舍信息
    @PostMapping
    public Dormitory add(@RequestBody Dormitory dormitory) {
        return dormitoryRepo.save(dormitory);
    }

    // 根据 ID 更新宿舍信息
    @PutMapping("/{id}")
    public Dormitory update(@PathVariable Long id, @RequestBody Dormitory dormitory) {
        dormitory.setId(id);  // 使用传入的 ID
        return dormitoryRepo.save(dormitory);
    }

    // 根据 ID 删除宿舍信息
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dormitoryRepo.deleteById(id);
    }
}