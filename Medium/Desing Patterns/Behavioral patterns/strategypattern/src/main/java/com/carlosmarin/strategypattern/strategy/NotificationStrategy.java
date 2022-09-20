package com.carlosmarin.strategypattern.strategy;

import com.carlosmarin.strategypattern.strategy.enums.NotificationType;

public interface NotificationStrategy {

    void sendMessage(String message);

    NotificationType notificationType();
}
