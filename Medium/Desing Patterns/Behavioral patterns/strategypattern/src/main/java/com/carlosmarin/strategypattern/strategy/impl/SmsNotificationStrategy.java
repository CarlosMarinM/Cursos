package com.carlosmarin.strategypattern.strategy.impl;

import com.carlosmarin.strategypattern.strategy.NotificationStrategy;
import com.carlosmarin.strategypattern.strategy.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendMessage(String message) {
        log.info("message send to phone " + message);
    }

    @Override
    public NotificationType notificationType() {
        return NotificationType.SMS;
    }
}
