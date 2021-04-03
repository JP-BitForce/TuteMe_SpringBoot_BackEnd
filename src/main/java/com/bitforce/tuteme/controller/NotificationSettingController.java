package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.NotificationSetting;
import com.bitforce.tuteme.service.NotificationSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    @PostMapping
    public NotificationSetting setNotification(@RequestBody NotificationSetting notificationSetting,@RequestParam Long userId){
        return notificationSettingService.setNotification(notificationSetting,userId);
    }

    @GetMapping("/{userId}")
    public NotificationSetting getNotification(@PathVariable Long userId){
        return notificationSettingService.getNotification(userId);
    }

    @PutMapping("/{userId}")
    public NotificationSetting updateNotification(@RequestBody NotificationSetting notificationSetting, @PathVariable Long userId){
        return notificationSettingService.updateNotification(notificationSetting,userId);
    }
}
