package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.NotificationSetting;
import com.bitforce.tuteme.repository.NotificationSettingRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final UserRepository userRepository;

    public NotificationSetting setNotification(NotificationSetting notificationSetting,Long userId) {
        notificationSetting.setUser(userRepository.findById(userId).get());
        return notificationSettingRepository.save(notificationSetting);
    }

    public NotificationSetting getNotification(Long userId) {
        return notificationSettingRepository.findByUserId(userId);
    }

    public NotificationSetting updateNotification(NotificationSetting notificationSetting, Long userId) {
        NotificationSetting setting =notificationSettingRepository.findByUserId(userId);
        notificationSetting.setId(setting.getId());
        notificationSetting.setUser(setting.getUser());
        BeanUtils.copyProperties(notificationSetting,setting);
        notificationSettingRepository.save(setting);
        return setting;
    }
}
