package com.camundaSaas.BankingCustomerOnboarding.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.css.Counter;

import com.camundaSaas.BankingCustomerOnboarding.model.AddressModel;
import com.camundaSaas.BankingCustomerOnboarding.model.CustomerDetailsModel;
import com.camundaSaas.BankingCustomerOnboarding.model.EmialValidationModel;
import com.camundaSaas.BankingCustomerOnboarding.model.PersonalDetails;
import com.camundaSaas.BankingCustomerOnboarding.repo.AddressRepo;
import com.camundaSaas.BankingCustomerOnboarding.repo.BankingRepo;
import com.camundaSaas.BankingCustomerOnboarding.repo.PersonalRepo;
import com.camundaSaas.BankingCustomerOnboarding.service.BankingService;
import com.camundaSaas.BankingCustomerOnboarding.util.BankingCustomerUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.CreateProcessInstanceCommandStep1.CreateProcessInstanceCommandStep3;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;

@RestController
public class BankingController {

	@Autowired
	ZeebeClient zeebeClient;

	@Autowired
	private JavaMailSender javaMailSender;

	final RestTemplate rest = new RestTemplate();

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	BankingService bankingService;

	@Autowired
	BankingRepo bankingRepo;
	
	private static final AtomicInteger counter = new AtomicInteger(1);
	
	@Autowired
	AddressRepo addressRep;

	@Autowired
	CustomerDetailsModel customerDetailsModel;

	@Autowired
	EmialValidationModel emialValidationModel;
	
	@Autowired
	PersonalRepo person;

	@Autowired
	BankingCustomerUtilities bankingCustomerUtilities;

	@Value("${camunda-usertask-timeout-param}")
	private Long timeout;
	
	@Value("${project.upload-files:/default/path}")
	private String uploadFiles;

	@Value("${camunda-env}")
	private String environment;

	// final private String taskListUrl =
	// "http://localhost:8080/getActivedTaskList";
	final private String taskListUrl = "http://localhost:8080/getNextActivestiveTask";

	//////////////////// Start api ////////////////////////
	@CrossOrigin
	
	@RequestMapping(value = "/startWorkflow", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public long startWorkflow(@RequestBody EmialValidationModel customerDetails) throws TaskListException {

		ProcessInstanceEvent processInstEvent = zeebeClient.newCreateInstanceCommand()
				.bpmnProcessId("Banking_Customer_Onboarding_Process").latestVersion().variables(customerDetails).send()
				.join();

		long processInstanceKey = processInstEvent.getProcessInstanceKey();

		bankingService.saveEmailValidation(customerDetails);

		return processInstanceKey;

	}

	//////////////////// OTP Validiation api dummy////////////////////////////
	@CrossOrigin
	@GetMapping("/getOTP/{otp}")
	public JSONObject otpValidation(@PathVariable String otp) {

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader(
					"D:\\demospace\\Camunda Coe Completed Use Case\\BankingCustomerOnboarding\\src\\jsonFile\\OTPfile.json"));
			JSONObject jsonObject = (JSONObject) obj;

			Map mp = (Map) jsonObject;

			if (mp.containsValue(otp)) {

				System.out.println("OTP is correct");

				zeebeClient.newPublishMessageCommand().messageName("otpVerification").correlationKey("12345").send()
						.join();

				return jsonObject;

			} else {

				System.out.println("OTP is incorrect");
				JSONObject errorResponse = new JSONObject();
				errorResponse.put("error", "Invalid OTP");
				return errorResponse;
			}

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the IOException if the file path is incorrect or the file cannot be
			// read
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "Failed to read the OTP file");

			return errorResponse;
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any other exception that may occur during parsing or processing
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "An unexpected error occurred");

			return errorResponse;
		}
	}

	/// original get otp///
	@CrossOrigin
	@GetMapping("/enterOTP/{enterOtp}/{processInstanceKey}")
	public JSONObject getNextActivieTask(@PathVariable String enterOtp, @PathVariable Long processInstanceKey) {

		String url = "http://localhost:8080/getNextActiveTaskDifference/" + processInstanceKey;

		JSONParser parser = new JSONParser();
		bankingCustomerUtilities = new BankingCustomerUtilities();
		try {
			Object obj = parser.parse(new FileReader(
					"D:\\demospace\\Camunda Coe Completed Use Case\\BankingCustomerOnboarding\\src\\jsonFile\\OTPfile.json"));
			JSONObject jsonObject = (JSONObject) obj;

			Map mp = (Map) jsonObject;

			if (mp.containsValue(enterOtp)) {

				System.out.println("OTP is correct");

				zeebeClient.newPublishMessageCommand().messageName("otpVerification").correlationKey("12345").send()
						.join();

				// Map taskListFormMap = bankingCustomerUtilities.getNextTaskForm(url);

				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("OTP", "OTP is correct");
				// jsonResponse.put("extractedInfo", taskListFormMap);

				return jsonResponse;

			} else {

				System.out.println("OTP is incorrect");
				JSONObject errorResponse = new JSONObject();
				errorResponse.put("error", "Invalid OTP");
				return errorResponse;
			}

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the IOException if the file path is incorrect or the file cannot be
			// read
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "Failed to read the OTP file");

			return errorResponse;
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any other exception that may occur during parsing or processing
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "An unexpected error occurred");

			return errorResponse;
		}

	}

	//////////////////// ElasticSearch /////////////////////

//	@GetMapping("/getNextActivestiveTask")
//	public Map<String, Object> getNextActivestiveTask() throws JsonMappingException, JsonProcessingException {
//
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.APPLICATION_JSON);
//
//		//String queryString = "{\"query\":{\"term\":{\"name\":\"localTaskName\"}}}";
//
//		HttpEntity<String> requestEntity = new HttpEntity<String>(header);
//
//		ResponseEntity<String> responseEntity = rest.exchange("http://localhost:9200/operate-list-view-1.3.0_*/_search",
//				HttpMethod.POST, requestEntity, String.class, header);
//		String body = responseEntity.getBody();
//
//		System.out.println(body);
//
//		ObjectMapper obj = new ObjectMapper();
//
//		Map responseMap = obj.readValue(body, Map.class);
//
//		Map getHitsMap = (Map) responseMap.get("hits");
//
//		Map<String,Object> getActiveTask = new HashMap();
//
//		
//		List hitsVariableList = (List) getHitsMap.get("hits");
//
//		for (Object hitsVariable : hitsVariableList) {
//
//			Map hitsVariableMap = (Map) hitsVariable;
//
//			Map sourceMap = (Map) hitsVariableMap.get("_source");
//			
//			if(sourceMap.containsKey("varName") && !sourceMap.get("varName").toString().isEmpty()) {
//				
//				String varName = (String) sourceMap.get("varName");
//				
//				System.out.println("  varName----> "+ varName);
//				
//				if(varName.equalsIgnoreCase("localTaskName")) {
//				
//				String getvarValue = (String) sourceMap.get("varValue");
//				Long getScopeKey = (Long)sourceMap.get("scopeKey");
//
//				String varValue = getvarValue.replaceAll("\\\"", "");
//
//				System.out.println("value   ------------" + getvarValue);
//				getActiveTask.put(varName, varValue);
//				getActiveTask.put("getScopeKey",getScopeKey);
//				}
//				
//			}
//
//			
//			
//		}
//
//		return getActiveTask;
//
//	}
//	

//	private List<Map<String, String>> getNextActiveTasks(Long processInstanceKey, String string) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@GetMapping("/getNextActivestiveTask/{processInstanceKey}")
	public List<Map<String, String>> getNextActivestiveTask(@PathVariable Long processInstanceKey,
			@RequestParam String intentState) throws JsonMappingException, JsonProcessingException {

		// String intentstate ="CREATED";

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		String queryString = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"value.processInstanceKey\":"
				+ processInstanceKey + "}},{\"term\":{\"intent\":" + "\"" + intentState + "\"}}]}}}";

		System.out.println("queryString - " + queryString);

		HttpEntity<String> requestEntity = new HttpEntity<String>(queryString, header);

		ResponseEntity<String> responseEntity = rest.exchange("http://localhost:9200/zeebe-record_job_*/_search",
				HttpMethod.POST, requestEntity, String.class, header);

		String body = responseEntity.getBody();

		System.out.println(body);

		ObjectMapper obj = new ObjectMapper();

		Map responseMap = obj.readValue(body, Map.class);

		Map getHitsMap = (Map) responseMap.get("hits");

		// Map<String,Object> getActiveTask = new HashMap();
		List list = new ArrayList();

		List hitsVariableList = (List) getHitsMap.get("hits");

		for (Object hitsVariable : hitsVariableList) {

			Map hitsVariableMap = (Map) hitsVariable;

			Map sourceMap = (Map) hitsVariableMap.get("_source");

			Map getValuMap = (Map) sourceMap.get("value");

			String elementInstanceKey = getValuMap.get("elementInstanceKey").toString();

			String jobKey = sourceMap.get("key").toString();
			Map<String, String> activeTaskMap = new HashMap();
			// activeTaskMap.put(elementInstanceKey, jobKey);

			activeTaskMap.put(jobKey, elementInstanceKey);
//			getActiveTask.put("elementInstanceKey", elementInstanceKey);
//			getActiveTask.put("jobKey",jobKey);
			list.add(activeTaskMap);

		}
		System.out.println("list[[[[" + list);
		return list;

	}

	@GetMapping("/getNextActiveTaskDifference/{processInstanceKey}")
	public List<Map<String, String>> getNextActiveTaskDifference(@PathVariable Long processInstanceKey)
			throws JsonMappingException, JsonProcessingException {
		List<Map<String, String>> createdTasks = getNextActivestiveTask(processInstanceKey, "CREATED");
		List<Map<String, String>> completedTasks = getNextActivestiveTask(processInstanceKey, "COMPLETED");

		System.out.println("createdTasks---->" + createdTasks);

		System.out.println("completedTasks---->" + completedTasks);
		// Find the difference between createdTasks and completedTasks
		List<Map<String, String>> difference = new ArrayList<>(createdTasks);
		difference.removeAll(completedTasks);
		System.out.println("createdTasks---->" + difference);

		return difference;
	}

///////////////////////////////// Address form task api //////////////////////////////

// Get Active User Task from TaskList
	@CrossOrigin
	@GetMapping("/getActivedTaskList")
	public List<Task> getActivedTaskList() throws TaskListException {

		List<Task> task = bankingCustomerUtilities.getTaskListClient(environment).getTasks(true, TaskState.CREATED, 50,
				true);
		// CamundaTaskListClient task =
		// bankingCustomerUtilities.getTaskListClient("saas");

		return task;

	}

// complete task using process instance id

	@CrossOrigin
	@PostMapping("/completeTaskWithInstanceId/{processInstanceKey}")
	public String completeTaskWithInstanceId(@PathVariable String processInstanceKey, @RequestBody Map variableMap)
			throws Exception {

//		String activeUrl = taskListUrl;
		String activeUrl = "http://localhost:8080/getActivedTaskList";
		ResponseEntity<List> getActiveTaskList = rest.getForEntity(activeUrl, List.class);

		Map mp = new HashMap();
		List activeTaskList = getActiveTaskList.getBody();

		List finalJobkey = new ArrayList();

		for (Object getTraceId : activeTaskList) {

			Map activeTaskMap = (Map) getTraceId;

			List<Object> getVariableList = (List<Object>) activeTaskMap.get("variables");

			if (getVariableList != null) {

				for (Object getVariable : getVariableList) {

					Map getVariableMap = (Map) getVariable;

					String getIds = (String) getVariableMap.get("id");

					String[] str = getIds.split("-");

					String stringGetprocessInstanceKey = str[0];

					if (processInstanceKey.equals(stringGetprocessInstanceKey)) {

						String jobKey = (String) activeTaskMap.get("id");

						finalJobkey.add(jobKey);

					}

					String jobKey = (String) activeTaskMap.get("id");

					finalJobkey.add(jobKey);

				}
			}

		}

		String taskId = (String) finalJobkey.get(0);

		Task task = bankingCustomerUtilities.getTaskListClient(environment).completeTask(taskId, variableMap);

		return " User Task Completed Successfully ";

	}

	//////////////////////////////////////////////////////////

	@CrossOrigin
	@PostMapping("/redirectApi/{processInstanceKey}")
	public String redirectApi(@PathVariable String processInstanceKey, @RequestBody Map variable) throws Exception {

		String activeUrl = taskListUrl;
		ResponseEntity<List> getActiveTaskList = rest.getForEntity(activeUrl, List.class);

		Map mp = new HashMap();
		List activeTaskList = getActiveTaskList.getBody();

		List finalJobkey = new ArrayList();

		for (Object getTraceId : activeTaskList) {

			Map activeTaskMap = (Map) getTraceId;

			List<Object> getVariableList = (List<Object>) activeTaskMap.get("variables");

			if (getVariableList != null) {

				for (Object getVariable : getVariableList) {

					Map getVariableMap = (Map) getVariable;

					String getIds = (String) getVariableMap.get("id");

					String[] str = getIds.split("-");

					String stringGetprocessInstanceKey = str[0];

					if (processInstanceKey.equals(stringGetprocessInstanceKey)) {

						String jobKey = (String) activeTaskMap.get("id");

						finalJobkey.add(jobKey);

					}

					String jobKey = (String) activeTaskMap.get("id");

					finalJobkey.add(jobKey);

				}
			}

		}

		String taskId = (String) finalJobkey.get(0);
		System.out.println(taskId);
		return taskId;
	}

/////////////////////// get all data from database  ////////////////////

	@GetMapping("/getAllDetails")
	public List<CustomerDetailsModel> getAllPersons() {
		return bankingRepo.findAll();
	}

///////////////////// get by id ////////////////////////////////

	@GetMapping("/getbyId/{id}")
	public Optional<CustomerDetailsModel> getbyId(@PathVariable int id) {
		return bankingRepo.findById(id);
	}

/////////////////// Email validation ///////////////////////////////

//	 @PostMapping("/login")
//	    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
//		 
//	        boolean isValidUser = bankingService.isUserValid(email, password);
//	        if (isValidUser) {
//	            return ResponseEntity.ok("Login successful!");
//	        } else {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
//	        }
//	    }
	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {

		boolean isValidUser = bankingService.validateCredentials(email, password);
		if (isValidUser) {
			return ResponseEntity.ok("Login successful!");
		}

		return null;

	}

	//////////////////////////////// complete user task /////////////////////////

	@CrossOrigin
	@RequestMapping(value = "/completeTask/{taskId}", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public Task claimTask(@PathVariable String taskId, @RequestBody String environment) throws TaskListException {

		System.out.println("Complete Task");

		Task task = bankingCustomerUtilities.getTaskListClient(environment).completeTask(taskId, null);

		return task;

	}
	////////////////////////////// job key for complete user task /////////////////

	@CrossOrigin
	@PostMapping("/completeUserTask/{processInstanceKey}")
	public JSONObject getNextAct(@RequestBody Map variableMap, @PathVariable Long processInstanceKey)
			throws TaskListException, InterruptedException, JsonMappingException, JsonProcessingException {
		 JSONObject jsonResponse = new JSONObject();

		String taskId = (String) variableMap.get("activeTaskId");
		String taskName = "";
		int creditScore = 0;

		variableMap.remove("activeTaskId");
		String bpmnProcessId = (String) variableMap.get("bpmnProcessId");

		if (bpmnProcessId.equalsIgnoreCase("Address_Verification")) {
			taskName = "Address Form";
		} else if (bpmnProcessId.equalsIgnoreCase("Personal_Details")) {
			taskName = "Personal Detail Form";
		}

		bankingCustomerUtilities = new BankingCustomerUtilities();

//		ProcessInstanceResult workflowInstanceResult = ((CreateProcessInstanceCommandStep3) zeebeClient
//				.newCreateInstanceCommand().bpmnProcessId(bpmnProcessId).latestVersion().variables(variableMap)
//				.requestTimeout(Duration.ofMillis(timeout))).withResult().send().join();

//		Map wfIR = workflowInstanceResult.getVariablesAsMap();
		int uniqueId = counter.getAndIncrement();
		if (bpmnProcessId.equalsIgnoreCase("Personal_Details")) {

		    // Remove bpmnProcessId before converting the map to the model
		    variableMap.remove("bpmnProcessId");

		    variableMap.put("creditScore", creditScore);

		    int annualIncome = (int) variableMap.get("annualIncome");
		    int age = (int) variableMap.get("age");

		    ObjectMapper mp = new ObjectMapper();

		    int baseScore = 300;
		    int ageFactor = 10;

		    int ageScore = age * ageFactor;
		    double incomeScore = annualIncome / 1000.0;

		    int credit = baseScore + ageScore + (int) incomeScore;

		    // Update the map with calculated credit score
		    variableMap.put("age", age);
		    variableMap.put("annualIncome", annualIncome);
		    variableMap.put("creditScore", credit);

		    // Convert the map to the model after removing unnecessary fields
		    CustomerDetailsModel personalInput = mp.convertValue(variableMap, CustomerDetailsModel.class);

		    System.out.println("variableMap ===" + personalInput.getCreditScore());
		   
		    jsonResponse.put("creditScore", credit);
		    

			 
			
			PersonalDetails personalObj=new PersonalDetails();
			personalObj.setPersonalId(uniqueId);
			personalObj.setAccountType((String)variableMap.get("accountType"));
			personalObj.setAge((int)variableMap.get("age"));
			personalObj.setAnnualIncome((int)variableMap.get("annualIncome"));
			personalObj.setDob((String)variableMap.get(("dob")));
			personalObj.setAge((int)variableMap.get("age"));
			
			
			person.save(personalObj);
		    
		}
		
		
		
		System.out.println("--"+variableMap.get("address"));
		
		String address= (String)variableMap.get("address");
		
		AddressModel obj=new  AddressModel();
		
		obj.setAddress(address);
		
		addressRep.save(obj);
		
		jsonResponse.put("id", uniqueId);
		System.out.println("user entered");
		String taskIdMod = bankingCustomerUtilities.getActiveTaskIdForTaskName("", taskName);

		try {
			Task task = bankingCustomerUtilities.getTaskListClient(environment).completeTask(taskIdMod, variableMap);
			System.out.println(" User Task Completed Successfully :" + taskIdMod);
		} catch (Exception ex) {
			System.out.println("task id not found..." + taskIdMod);
		}
		return jsonResponse ;
		
		

	}
		

//		variableMap.put("wfIR", wfIR);


//
//		// String url = "http://localhost:8080/getNextActiveTaskDifference/" +
//		// processInstanceKey;
//
//		// Map taskListFormMap = bankingCustomerUtilities.getNextTaskForm(url);
//
//		JSONObject jsonResponse = new JSONObject();
//		
//		jsonResponse.put("creditScore", creditScore);
//		System.out.println("***********creditScore"+creditScore);

//		if (null != taskListFormMap && taskListFormMap.size() > 0) {
//
//			jsonResponse.put("extractedInfo", taskListFormMap);
//			System.out.println("jsonResponse.." + jsonResponse.toJSONString());
//		} else {
//			System.out.println("Entering Sleep Mode....");
//			// Thread.sleep(20000);
//			System.out.println("Exit Sleep Mode....");
//			taskListFormMap = bankingCustomerUtilities.getNextTaskForm(url);
//			jsonResponse.put("extractedInfo", taskListFormMap);
//			System.out.println("jsonResponse.." + jsonResponse.toJSONString());
//
//		}

	
	

	/////////////////////////// Assigine task //////////////////////////

	@CrossOrigin

	@GetMapping("/getAssignedTask/{userName}")

	public List<Task> getAssignedTask(@PathVariable String userName) throws TaskListException {

		List<Task> task = bankingCustomerUtilities.getTaskListClient(environment).getAssigneeTasks(userName,
				TaskState.CREATED, 50, true);

		return task;

	}

	@CrossOrigin

	@GetMapping("/assignedTaskList")

	public List<Object> getTaskList(@RequestParam String userName) {

		String url = "http://localhost:8080/getAssignedTask/" + userName;
		System.out.println("url - " + url);
		RestTemplate restTemplate = new RestTemplate();

		List<Object> result = restTemplate.getForObject(url, List.class);

		List<Object> finalTaskList = new ArrayList<Object>();

		for (Object getList : result) {

			Map getListAsMap = (Map) getList;

			String getId = (String) getListAsMap.get("id");

			String getName = (String) getListAsMap.get("name");

			String getAssignee = (String) getListAsMap.get("assignee");

			String getcreationTime = (String) getListAsMap.get("creationTime");

			String[] str = getcreationTime.split("T");

			String creationTime = str[0];

			Map<String, Object> finalMap = new HashMap<String, Object>();

			finalMap.put("id", getId);

			finalMap.put("name", getName);

			finalMap.put("assignee", getAssignee);

			finalMap.put("creationTimes", creationTime);

			finalTaskList.add(finalMap);

		}

		return finalTaskList;

	}

	/////////////////////////////// consolidate variable ///////////////////////

	@CrossOrigin
	@GetMapping("/getTaskvariable/{jobKey}")
	public Task getvariabletask(@PathVariable String jobKey) throws TaskListException {

		System.out.println(jobKey);

		Task task = bankingCustomerUtilities.getTaskListClient(environment).getTask(jobKey, true);

		return task;

	}

	@CrossOrigin
	@GetMapping("/getConsolidateVariable/{jobKey}")
	public Map<String, Object> getConsolidateVariable(@PathVariable String jobKey) {

		String url = "http://localhost:8080/getTaskvariable/" + jobKey;

		System.out.println(" url --" + url);
		RestTemplate restTemplate = new RestTemplate();
		Map result = restTemplate.getForObject(url, Map.class);

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> consolidatedVariables = new HashMap<>();
		
		

		try {

			Map<String, Object> responseList = objectMapper.readValue(objectMapper.writeValueAsString(result),
					new TypeReference<Map<String, Object>>() {
					});

			List<Map<String, Object>> variables = (List<Map<String, Object>>) responseList.get("variables");

			for (Map<String, Object> variable : variables) {
				String variableName = (String) variable.get("name");
				Object variableValue = variable.get("value");
				consolidatedVariables.put(variableName, variableValue);
			}

			System.out.println(consolidatedVariables);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		 String imagePath = "D:\\demospace\\Camunda Coe Completed Use Case\\BankingCustomerOnboarding\\uploadFiles\\cca801e3-a737-489c-86ea-47b2417a5b5c.docx";
		    
		    // Read the image file into a byte array
		    try {
		        byte[] imageBytes;
		        try (InputStream inputStream = new FileInputStream(imagePath)) {
		            imageBytes = new byte[(int) new File(imagePath).length()];
		            inputStream.read(imageBytes);
		        }

		        // Convert to Base64 for safe transmission
		        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

		        // Add the image and filename to consolidatedVariables
		        consolidatedVariables.put("image", base64Image);
		        consolidatedVariables.put("imageFileName", imagePath);

		    } catch (IOException e) {
		        e.printStackTrace();
		        consolidatedVariables.put("error", "Failed to read the image file");
		    }

		
		
		
		return consolidatedVariables;

	}

////////////////////////////customer reply ///////////////////////////////////////////////////////

	@CrossOrigin
	@PostMapping("/customerReply")
	public Map<String, Object> customerReply(@RequestParam String processInstanceKey) throws Exception {

		// String activeUrl = taskListUrl;
		String activeUrl = "http://localhost:8080/getActivedTaskList";
		ResponseEntity<List> getActiveTaskList = rest.getForEntity(activeUrl, List.class);

		Map mp = new HashMap();
		List activeTaskList = getActiveTaskList.getBody();

		List finalJobkey = new ArrayList();

		String jobKey = null;

		Map<String, Object> consolidatedVariables = new HashMap<>();

		for (Object getTraceId : activeTaskList) {

			Map activeTaskMap = (Map) getTraceId;

			List<Object> getVariableList = (List<Object>) activeTaskMap.get("variables");

			if (getVariableList != null) {

				for (Object getVariable : getVariableList) {

					Map getVariableMap = (Map) getVariable;

					String getIds = (String) getVariableMap.get("id");

					String[] str = getIds.split("-");

					String stringGetprocessInstanceKey = str[0];

					if (processInstanceKey.equals(stringGetprocessInstanceKey)) {

						jobKey = (String) activeTaskMap.get("id");

						finalJobkey.add(jobKey);

						String url = "http://localhost:8080/getTaskvariable/" + jobKey;
						RestTemplate restTemplate = new RestTemplate();
						Map result = restTemplate.getForObject(url, Map.class);

						ObjectMapper objectMapper = new ObjectMapper();

						try {

							Map<String, Object> responseList = objectMapper.readValue(
									objectMapper.writeValueAsString(result), new TypeReference<Map<String, Object>>() {
									});

							List<Map<String, Object>> variables = (List<Map<String, Object>>) responseList
									.get("variables");

							for (Map<String, Object> variable : variables) {
								String variableName = (String) variable.get("name");
								Object variableValue = variable.get("value");
								consolidatedVariables.put(variableName, variableValue);
							}

							System.out.println(consolidatedVariables);
						} catch (IOException e) {
							e.printStackTrace();
						}
						// return consolidatedVariables;

					}

				}
			}

		}
		if (jobKey != null) {
			System.out.println(jobKey);

		}
		return consolidatedVariables;

	}

	////////////////

	@CrossOrigin
	@PostMapping("/completeTasklocal/{taskId}")
	//@RequestMapping(value = "/completeTasklocal/{taskId}", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public String claimTask(@PathVariable String taskId, @RequestBody Map map) throws TaskListException {

		System.out.println("Task Id.....: " + taskId);
		System.out.println("mapped...........: " + map);

		SimpleAuthentication sa = new SimpleAuthentication("demo", "demo");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().taskListUrl("http://localhost:8083")
				.shouldReturnVariables().authentication(sa).build();

		Task task = client.completeTask(taskId, map);

		return "Completed";

	}

	@CrossOrigin
	@GetMapping("/testApi")
	public String testApi() {

		try {
			bankingCustomerUtilities.getActiveTaskIdForTaskName("", "Address Form");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "ok";
	}
	
	//--download 
	
	@CrossOrigin
	@GetMapping("/downloadImage")
	public ResponseEntity<byte[]> downloadImage() {
	    String imagePath = "D:\\demospace\\Camunda Coe Completed Use Case\\BankingCustomerOnboarding\\uploadFiles\\cca801e3-a737-489c-86ea-47b2417a5b5c.docx"; // Your static file path
	    System.out.println("download");
	    try {
	        File imageFile = new File(imagePath);
	        byte[] imageBytes = new byte[(int) imageFile.length()];

	        try (InputStream inputStream = new FileInputStream(imageFile)) {
	            int bytesRead = inputStream.read(imageBytes);
	            if (bytesRead == -1) {
	                throw new IOException("Could not read file: " + imageFile.getAbsolutePath());
	            }
	        }

	        // Set the response headers to indicate a file download
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.builder("attachment")
	            .filename(imageFile.getName())
	            .build());
System.out.println("enter");
	        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	
	
	
	
	//--upload 
	@CrossOrigin
	@PostMapping("/files/fileimage/upload")
    public ResponseEntity<String> uploadFiles(@RequestPart("image") MultipartFile image) throws IOException {
        System.out.println("Upload initiated");

        // Get the original file name
        String originalFileName = image.getOriginalFilename();
        String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
        String randomID = UUID.randomUUID().toString();
        String fileName = randomID + fileExtension;

        // Ensure the upload directory exists
        File uploadDir = new File(uploadFiles);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Construct the full file path
        Path filePath = Paths.get(uploadFiles, fileName);
        System.out.println("Saving file to: " + filePath.toString());

        // Save the file manually
        try (InputStream inputStream = image.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return ResponseEntity.ok("File uploaded successfully. File name: " + fileName);
    }

}
