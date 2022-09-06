package com.carlosmarin.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://www.baeldung.com/spring-amqp
@SpringBootApplication
public class AmqpApplication implements ApplicationRunner {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(AmqpApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println("Hello World from Application Runner");
		rabbitTemplate.convertAndSend("myQueue", "Hello, world!");
	}

	@RabbitListener(queues = "myQueue")
	public void listen(String in) {
		System.out.println("Message read from myQueue : " + in);
	}
}
