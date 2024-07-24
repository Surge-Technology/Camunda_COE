package com.test.issue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
public class IssueApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssueApplication.class, args);
	}

}
