package com.camunda.orderfullfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient

public class OrderFullfillmentApplication {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Updated Version 1.4....");
		SpringApplication.run(OrderFullfillmentApplication.class, args);
		
	}

}
