package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.NotificationSetting;
import com.bitforce.tuteme.repository.NotificationSettingRepository;
import com.bitforce.tuteme.service.NotificationSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;
    private final NotificationSettingRepository settingRepository;

    @PostMapping
    public NotificationSetting setNotification(@RequestBody NotificationSetting notificationSetting,@RequestParam Long userId) {
        try {

            if (!settingRepository.findByUserId(userId).equals(null)) {
                return notificationSettingService.updateNotification(notificationSetting, userId);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return notificationSettingService.setNotification(notificationSetting, userId);
    }

    @GetMapping("/{userId}")
    public NotificationSetting getNotification(@PathVariable Long userId){
        return notificationSettingService.getNotification(userId);
    }

}
