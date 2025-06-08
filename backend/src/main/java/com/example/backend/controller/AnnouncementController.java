// AnnouncementController.java
package com.example.backend.controller;

import com.example.backend.entity.Announcement;
import com.example.backend.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // 获取公告
    @GetMapping
    public Announcement getAnnouncement() {
        return announcementRepository.findById(1L).orElse(new Announcement());  // 假设公告只有一条，ID 为 1
    }

    // 保存公告
    @PutMapping
    public Announcement saveAnnouncement(@RequestBody Announcement announcement) {
        announcement.setId(1L);  // 设置公告的 ID 为 1，确保只更新第一条公告
        return announcementRepository.save(announcement);
    }
}
