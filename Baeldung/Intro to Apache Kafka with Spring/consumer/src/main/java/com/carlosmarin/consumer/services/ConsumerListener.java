package com.carlosmarin.consumer.services;

import com.carlosmarin.consumer.models.Greeting;
import org.springframework.kafka.annotation.KafkaListener;

public class ConsumerListener {

    @KafkaListener(topics = "baeldung", groupId = "baeldungGrp")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }

    @KafkaListener(
            topics = "baeldung",
            containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        System.out.println("Received Greeting in group foo: " + greeting);
    }
}
