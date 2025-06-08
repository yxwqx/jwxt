package com.example.backend.controller;

import com.example.backend.entity.Admin;
import com.example.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public String register(@RequestBody Admin admin) {
        Admin result = adminService.register(admin);
        return result != null ? "注册成功" : "用户名已存在";
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin admin) {
        Admin result = adminService.login(admin.getUsername(), admin.getPassword());
        return result != null ? "登录成功" : "用户名或密码错误";
    }

    @PutMapping("/update")
    public Admin updateInfo(@RequestBody Admin admin) {
        return adminService.updateInfo(admin);
    }

    @GetMapping
    public List<Admin> list() {
        return adminService.getAllAdmins();
    }
}
