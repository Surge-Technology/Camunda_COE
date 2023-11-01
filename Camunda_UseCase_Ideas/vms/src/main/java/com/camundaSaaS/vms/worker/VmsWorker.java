package com.camundaSaaS.vms.worker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
public class VmsWorker {

	@Autowired
	ZeebeClient zeebeClient;

	final RestTemplate rest = new RestTemplate();

	@ZeebeWorker(type = "approved", name = "approved")
	public void approvedMail(final JobClient client, final ActivatedJob job) {

		System.out.println("Approved worker started");

		// String variableMap = job.getVariables();
		// System.out.println(variableMap);

		// final Topology topology = zeebeClient.newTopologyRequest().send().join();

		client.newCompleteCommand(job.getKey()).variables("").send().join();

		System.out.println("approved flow end");

	}

	@ZeebeWorker(type = "rejected", name = "rejected")
	public void rejectedMail(final JobClient client, final ActivatedJob job) {

		System.out.println("rejected worker started");

		client.newCompleteCommand(job.getKey()).variables("").send().join();

		System.out.println("rejected flow end");

	}

	@ZeebeWorker(type = "postClarification", name = "postClarification")
	public void clarificationMail(final JobClient client, final ActivatedJob job) {

		System.out.println("postClarification worker started");

		Map getVariable = job.getVariablesAsMap();
		System.out.println("post clarification worker getVariable - " + getVariable);
		Long processInstanceKey = job.getProcessInstanceKey();
		System.out.println("processInstanceKey - " + processInstanceKey);

		String message = (String) getVariable.get("message");

		String approverAction = (String) getVariable.get("approverAction");

		System.out.println("approverAction --" + approverAction);

		Map clarificationVariableMap = new HashMap();
		
		clarificationVariableMap.put("processInstanceKey", processInstanceKey);
		clarificationVariableMap.put("approverAction", approverAction);

		clarificationVariableMap.put("getVariable", getVariable);
		
		System.out.println("clarificationVariableMap -- "+ clarificationVariableMap);

		String urlKafka = "http://localhost:8081/" + message;

		System.out.println("urlKafka -" + urlKafka);

		String response = rest.getForObject(urlKafka, String.class);

		client.newCompleteCommand(job.getKey()).variables(clarificationVariableMap).send().join();

	}

}
