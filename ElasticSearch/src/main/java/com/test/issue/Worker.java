package com.test.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@Service
public class Worker {

	@Autowired
	ZeebeClient zeebeClient;
	
	
	@ZeebeWorker(type="Serviceone",name="Serviceone")
	public void method1(final JobClient client,final ActivatedJob job) {
		System.out.println("method1...........");
		zeebeClient.newCompleteCommand(job.getKey()).variables("").send().join();
	}
	@ZeebeWorker(type="Servicetwo",name="Servicetwo")
	public void method2(final JobClient client,final ActivatedJob job) {
		System.out.println("method2...........");
		zeebeClient.newCompleteCommand(job.getKey()).variables("").send().join();
	}
	@ZeebeWorker(type="Servicethree",name="Servicethree")
	public void method3(final JobClient client,final ActivatedJob job) {
		System.out.println("method3...........");
		zeebeClient.newCompleteCommand(job.getKey()).variables("").send().join();
	}
}
