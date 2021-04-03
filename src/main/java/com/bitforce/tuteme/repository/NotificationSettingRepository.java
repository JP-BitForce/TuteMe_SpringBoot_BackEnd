package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting,Long> {
    NotificationSetting findByUserId(Long userId);
}
