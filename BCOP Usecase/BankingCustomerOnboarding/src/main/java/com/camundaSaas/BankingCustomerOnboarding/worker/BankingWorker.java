package com.camundaSaas.BankingCustomerOnboarding.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.camundaSaas.BankingCustomerOnboarding.model.AddressModel;
import com.camundaSaas.BankingCustomerOnboarding.model.CustomerDetailsModel;
import com.camundaSaas.BankingCustomerOnboarding.model.PersonalDetails;
import com.camundaSaas.BankingCustomerOnboarding.repo.AddressRepo;
import com.camundaSaas.BankingCustomerOnboarding.repo.BankingRepo;
import com.camundaSaas.BankingCustomerOnboarding.repo.PersonalRepo;
import com.camundaSaas.BankingCustomerOnboarding.service.BankingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
public class BankingWorker {

	@Autowired
	ZeebeClient client;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	CustomerDetailsModel customerDetailsModel;

	@Autowired
	BankingService bankingService;

	@Autowired
	AddressModel addressModel;

	@Autowired
	PersonalDetails personalDetails;

	@Autowired
	BankingRepo bankingRepo;

	@Autowired
	PersonalRepo personalRepo;

	final RestTemplate rest = new RestTemplate();

//////////////////////////worker 1 - sendingEmailVerification /////////////////

	@ZeebeWorker(name = "Sending Otp to email", type = "sendingOtptoEmail")
	public void sendingEmailVerification(final JobClient client, final ActivatedJob job) throws IOException {

		Map variableMap = job.getVariablesAsMap();

		Long pId = job.getProcessInstanceKey();

		String OTPVALUES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		Random rndm_method = new Random();

		String OTP = "";

		int len = 6;

		String OTPemail = null;
		char[] password = new char[len];
		Map OTPmap = new HashMap<>();

		for (int i = 0; i < len; i++)

		{

			password[i] =

					OTPVALUES.charAt(rndm_method.nextInt(OTPVALUES.length()));
			OTPmap.put("OTP", password);
		}

		String str = new String(password);
		OTPemail.valueOf(password);

		System.out.println(password);

		ObjectMapper mapper = new ObjectMapper();
		File file = new File(
				"D:\\Workspace\\cop bank\\FinalBcop\\BankingCustomerOnboarding\\src\\jsonFile\\OTPfile.json");

		if (!file.exists()) {
			file.createNewFile();
		}
		Map productList = new HashMap<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			TypeReference<Map> typeReference = new TypeReference<Map>() {
			};
			productList = mapper.readValue(fileInputStream, typeReference);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			FileWriter fileWriter = new FileWriter(file);
			mapper.writeValue(fileWriter, OTPmap);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String sender = "balamanchari@gmail.com";
		String receiver = "camerongre1@gmail.com";

		String subject = "Email Verification Confirmation";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(subject);

		System.out.println("otp : " + str);
		mailMessage.setText("OTP is " + str);

		System.out.println(mailMessage);
		javaMailSender.send(mailMessage);

		client.newCompleteCommand(job.getKey()).variables(variableMap).send().join();
		System.out.println("sendingEmailVerification id Completed");

	}

///////////////////////////////// Address Worker /////////////////////////////////
	@CrossOrigin
	@ZeebeWorker(name = "addressWorker", type = "addressWorker")
	public void addressWorker(final JobClient client, final ActivatedJob job) throws Exception {
		System.out.println("addres worker entered");

		Map<String, Object> variableMap = job.getVariablesAsMap();

		System.out.println(" variableMap -" + variableMap);
		String addressFromUser = (String) variableMap.get("address");

		String status = "approved";
		variableMap.put("status", status);

		System.out.println("variable Map == " + variableMap);
		String key = "7NQ8LYVK4C9FTJ3H6WMG";
		String format = "json";
		String secret = "6EYGMU3Q94RVXTKAFPC7";
		String address = (String) variableMap.get("address");

		Map mpm = (Map) variableMap.get("input");

		ObjectMapper mp = new ObjectMapper();
		CustomerDetailsModel convert = mp.convertValue(mpm, CustomerDetailsModel.class);

		Object addresss = variableMap.get("address");

		StringBuilder response = new StringBuilder();

		String encodedAddress = URLEncoder.encode(address, "UTF-8");

		String url = "https://api.addressfinder.io/api/au/address/v2/verification/?key=" + key + "&secret=" + secret
				+ "&format=" + format + "&q=" + encodedAddress + "&gnaf=1&paf=1";
		System.out.println("url : " + url);

		URL url1 = new URL(url);

		HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String line;

		while ((line = reader.readLine()) != null) {

			response.append(line);

		}

		reader.close();

		String jsonString = response.toString();

		System.out.println("jsonString----" + jsonString);

		JSONObject jsonObject = new JSONObject(jsonString);
		boolean matched = jsonObject.getBoolean("matched");
		variableMap.put("matched", matched);

		if (matched) {

			System.out.println(" Address is Valid ");

		} else {
			System.out.println("InValid or not found address");

		}

		client.newCompleteCommand(job.getKey()).variables(variableMap).send().join();

	}

	/////////////////// credit score generation worker /////////////

	@ZeebeWorker(name = "generated credit score", type = "generatedCreditScore")
	public void generateCreditScore(final JobClient client, final ActivatedJob job) {
		System.out.println("generate credit score worker entered");

		Map<String, Object> getVariable = job.getVariablesAsMap();
		String firstName = (String) getVariable.get("firstName");
		String lastName = (String) getVariable.get("lastName");
		int age = (int) getVariable.get("age");
		String gender = (String) getVariable.get("gender");
		String dob = (String) getVariable.get("dob");
		int phoneNo = (int) getVariable.get("phoneNo");
		int annualIncome = (int) getVariable.get("annualIncome");
		String accountType = (String) getVariable.get("accountType");

		ObjectMapper mp = new ObjectMapper();

		int baseScore = 300;
		int ageFactor = 10;
		int ageScore = age * ageFactor;
		double incomeScore = annualIncome / 1000;
		int creditScore = baseScore + ageScore + (int) incomeScore;

		Map variableMap = new HashMap();
		variableMap.put("firstName", firstName);
		variableMap.put("lastName", lastName);
		variableMap.put("age", age);
		variableMap.put("gender", gender);
		variableMap.put("dob", dob);
		variableMap.put("phoneNo", phoneNo);
		variableMap.put("annualIncome", annualIncome);
		variableMap.put("accountType", accountType);
		variableMap.put("creditScore", creditScore);
		// getVariable.put("creditScore", creditScore);
		CustomerDetailsModel personalInput = mp.convertValue(variableMap, CustomerDetailsModel.class);

		System.out.println("variableMap ===" + personalInput);

		client.newCompleteCommand(job.getKey()).variables(variableMap).send().join();

		bankingService.saveCustomerDetails(personalInput);

		CustomerDetailsModel customerDetailsModel = new CustomerDetailsModel();

	}

///////////////////////  DeliverConfirmation worker ///////////////////////

	@ZeebeWorker(name = "Add Approved Application to CRM", type = "Add Application into CRM")
	public void DeliverConfirmationWorker(final JobClient client, final ActivatedJob job) {
		System.out.println("DeliverConfirmation worker entered");

		client.newCompleteCommand(job.getKey()).variables("").send().join();

	}

///////////////////////  DeliverConfirmation worker ///////////////////////

	@ZeebeWorker(name = "Send Confirmation on Application Acceptance", type = "Send Confirmation")
	public void SendConfirmation(final JobClient client, final ActivatedJob job) {
		System.out.println("Send Confirmation on Application Acceptance worker entered");

		String str = "Hello, Cameron Green\n\n We are delighted to inform you that your bank account has been successfully created with our bank. Welcome to our banking family!";
		String sender = "balamanchari@gmail.com";
		String receiver = "camerongre1@gmail.com";
		String subject = "Welcome to Our Bank";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(subject);
		mailMessage.setText(str);
		System.out.println(mailMessage);
		javaMailSender.send(mailMessage);

		client.newCompleteCommand(job.getKey()).variables("").send().join();

	}

//////////////////////Required Document worker ////////////////////

	@ZeebeWorker(name = "Send Clarification", type = "sendClarification")
	public void requiredDocumentWorker(final JobClient client, final ActivatedJob job) {

		System.out.println("Required Document worker entered");

		Map getVariableMap = job.getVariablesAsMap();
		Long pId = job.getProcessInstanceKey();

		String str = "Hello, Here is your link: <a href='http://localhost:3000/CustomerQueryPage?pId=" + pId
				+ "'>Click here</a>";
		String sender = "balamanchari@gmail.com";
		String receiver = "camerongre1@gmail.com";
		String subject = "Email Verification Confirmation";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(subject);
		mailMessage.setText(str);
		System.out.println(mailMessage);
		javaMailSender.send(mailMessage);

		client.newCompleteCommand(job.getKey()).variables(getVariableMap).send().join();

	}

	@ZeebeWorker(name = "Add Rejected Application to CRM", type = "addRejectedApplication to CRM")
	public void rejectApplication(final JobClient client, final ActivatedJob job) {

		System.out.println("Reject Application worker entered");

		client.newCompleteCommand(job.getKey()).variables("").send().join();
	}

	@ZeebeWorker(name = "Send Notification on Application Rejection", type = "Send rejection")
	public void sendRejection(final JobClient client, final ActivatedJob job) {

		System.out.println("Send rejection worker entered");

		String str = "Hello, Cameron Green\n\nWe regret to inform you that your application has been reviewed, and we are unable to proceed with it at this time. We appreciate your interest and the effort you put into your application.";
		String sender = "balamanchari@gmail.com";
		String receiver = "camerongre1@gmail.com";
		String subject = "Application Status: Regrettably, Your Application Has Not Been Approved";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(subject);
		mailMessage.setText(str);
		System.out.println(mailMessage);
		javaMailSender.send(mailMessage);

		client.newCompleteCommand(job.getKey()).variables("").send().join();
	}

}
