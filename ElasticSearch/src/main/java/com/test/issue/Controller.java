package com.test.issue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;

@RestController
public class Controller {

	@Autowired
	ZeebeClient zeebeClient;

	@PostMapping("/post/{totalCount}")
	public Object Start(@PathVariable Integer totalCount) {
		System.out.println("Before");
		ProcessInstanceResult processInstanceResult = null;
		try {
			
			
			for (int i = 0; i <= totalCount; i++) {
				

				processInstanceResult = zeebeClient.newCreateInstanceCommand()
						.bpmnProcessId("mdcFlow1").latestVersion().variables("").withResult().send().join();
				System.out.println(i +"============ process instance key.............." + processInstanceResult.getProcessInstanceKey());
			}
			System.out.println("After====");
		}catch(Exception ex) {
			System.out.println("Error Occured..."+ex.getMessage());
			return null;
		}
		
		return "instance started successfully....:"+processInstanceResult.getProcessInstanceKey();
	}

	@GetMapping("/getPID/{proInstanceKey}")
	public String getProcessInstanceKeyDetails(@PathVariable String proInstanceKey) {
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate rest = new RestTemplate();
		String zeebeRecordURL = "http://localhost:9200/zeebe*/_search";
		
		String zeebeRecordURLTbd =  "http://localhost:9200/zeebe-record-process-instance/_search?size=10&from=9999";
		ResponseEntity<Map> responseEntity = rest.getForEntity(zeebeRecordURLTbd, Map.class);
		Map resBody = responseEntity.getBody();
		Map zeebeHitsObj = (Map) resBody.get("hits");
		List zeebeHitsObjList = (List) zeebeHitsObj.get("hits");
		Map zeebemp = null;
		Long processInstanceKey = null;
		long key = 0;
		for (Object zeeberef : zeebeHitsObjList) {
			zeebemp = (Map) zeeberef;
			Map zeebeSourceObj = (Map) zeebemp.get("_source");
			Map zeebeValueObj = (Map) zeebeSourceObj.get("value");
			//System.out.println("this is zeebeValueObj" + zeebeValueObj);

			if (zeebeValueObj.containsKey("processInstanceKey")) {
				processInstanceKey = (Long) zeebeValueObj.get("processInstanceKey");
				
				if(processInstanceKey == Long.parseLong(proInstanceKey)) {
					System.out.println("matched...:"+proInstanceKey);
					return "Matched: "+processInstanceKey;
				}
				System.out.println(processInstanceKey.longValue());
			}

			//return key;
		}
		
		return "Not Matched" ;
	}
}
