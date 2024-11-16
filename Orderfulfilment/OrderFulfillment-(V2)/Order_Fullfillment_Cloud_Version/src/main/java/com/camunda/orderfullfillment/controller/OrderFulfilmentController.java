
package com.camunda.orderfullfillment.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.json.JSONObject;
//import org.json.JSONArray;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camunda.orderfullfillment.model.BillingTo;
import com.camunda.orderfullfillment.model.Cart;
import com.camunda.orderfullfillment.model.InvoiceDetails;
import com.camunda.orderfullfillment.model.ProductDetailss;
import com.camunda.orderfullfillment.util.OrderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.protobuf.TextFormat.ParseException;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.dto.Variable;
import io.camunda.tasklist.dto.VariableType;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

@RestController

public class OrderFulfilmentController {

//	@Value("${multi-instance-example.number-of-buckets}")
//
//	public long numberOfBuckets;
//
//	@Value("${multi-instance-example.number-of-elements}")
//
//	public long numberOfElements;

//	ClassLoader classLoader = getClass().getClassLoader();
//	String jsonFilePath = classLoader.getResource("jsonreader/CustomerOrderHistory.json").getFile();
//	

//	String jsonFilePath = "D:/of-backend/Order_Fullfillment_Cloud_Version/src/main/resources/jsonreader/CustomerOrderHistory.json";

	@Autowired

	ZeebeClient zeebeClient;

	@Autowired

	ProductDetailss ds;

	ObjectMapper objectMapper = new ObjectMapper();

	final RestTemplate rest = new RestTemplate();

	@Autowired
	OrderUtil orderUtil;

	private AtomicLong orderIdCounter = new AtomicLong(1);

	@GetMapping("/test")

	public JSONArray demo() throws Exception {

		System.out.println("demo test called 1.2...");

		JSONArray jsonArray = orderUtil.readFileMock("AddProduct.json");

		System.out.println("demo test completed...");

		return jsonArray;

	}

//	@GetMapping("/test")
//	public String getData() throws Exception {
// 
//		String fileContent = readFile("classpath:jsonreader/AddProduct.json");
//		System.out.println(fileContent);
//		return fileContent;
//	
//	}

// All API's :

// OrderFulfilment API :-

// 1.

// ****************************** Start_WorkFlow_API's ****************************

//	@CrossOrigin
//	@PostMapping(value = "/submitProductList")
//	public Map<String, Object> startWorkflow(@RequestBody String reqBody) throws Exception {
//		long generatedOrderId = orderIdCounter.getAndIncrement();
//		
//		JSONObject jsonObj=new JSONObject(reqBody);
//		
//		System.out.println("user id---"+jsonObj.getString("userId"));
//
//		// Camunda-related logic ...
//		
//
//		generatedOrderId = orderIdCounter.getAndIncrement();
//		Map<String, Object> inputVariable = new HashMap<>();
//
//		// Authenticate with Camunda Task List
//		SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",
//				"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");
//
//		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
//				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
//				.shouldReturnVariables().authentication(sa).build();
//
//		// Parse the request body
//		try {
//			List<ProductDetailss> reqBodyMap = objectMapper.readValue(reqBody,
//					new TypeReference<List<ProductDetailss>>() {
//					});
//
//			System.out.println("reqBodyMap---" + reqBodyMap);
//
//			inputVariable.put("inputVaribale", reqBodyMap);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		ProcessInstanceEvent processInstEvnt = zeebeClient.newCreateInstanceCommand().bpmnProcessId("OrderFullfillment")
//				.latestVersion().variables(inputVariable).send().join();
//
//		long pID = processInstEvnt.getProcessInstanceKey();
//		long processDefinitionKey = processInstEvnt.getProcessDefinitionKey();
//
//		// Prepare the process map
//		Map<String, Object> processIdMap = new HashMap<>();
//	    processIdMap.put("processInstanceKey", pID);
//		processIdMap.put("orderId", generatedOrderId);
//		processIdMap.put("processInstanceKey", pID);
//		processIdMap.put("paymentStatus", " ");
//		processIdMap.put("productDetail", inputVariable);
//
//		try {
//
//			String jsonFilePath = orderUtil.readJsonFile("jsonreader/CustomerOrderHistory.json");
//
//			 String content = new String(Files.readAllBytes(new File(jsonFilePath).toPath()));
//			 
//			 List<Map<String, Object>> existingData = objectMapper.readValue(content, new TypeReference<List<Map<String, Object>>>() {});
//
//			existingData.add(processIdMap);
//
//			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), existingData);
//			System.out.println("Data updated successfully in: " + jsonFilePath);
//		} catch (IOException e) {
//			System.err.println("Error writing to JSON file: " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		return processIdMap;
//	}
//	
	
	
	
	// to get the customer history detail
	
//	@GetMapping(value = "/getCustomerHistory")
//	public List<Map<String, Object>> getHistory() throws org.json.simple.parser.ParseException {
//		
//		String jsonFilePath=orderUtil.readJsonFile("jsonreader/CustomerOrderHistory.json");
//		
//		
//		try {
//	        // Read the JSON file as a String
//	        String content = new String(Files.readAllBytes(new File(jsonFilePath).toPath()));
//	        
//	        System.out.println("content---"+content);
//	        
//	        JSONParser parser = new JSONParser();
//	        Object obj = null;
//			try {
//				obj = parser.parse(content);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        JSONArray jsonArray = (JSONArray) obj;
//	        
//	        return jsonArray;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//		
//		
//		
//	}

	

// 2.

// ****************************** Get_Active_UserTask_List ****************************

//////////////////////// Get Active User Task List //////////////////////

	@CrossOrigin()
	@GetMapping("/getActivedTaskList")

	public List<Task> getActivedTaskList() throws TaskListException {

		SaasAuthentication sa = new SaasAuthentication("CwDKV~wHD2FcVeHiA_Nvcl.GSVsQdFSf",

				"qY.Cw2iy52u7KSqumTNcjlOKiBnPK_68qpQtDKK_8SpEiG.WthMXAv2~fZIdT9v2");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		return client.getTasks(true, TaskState.CREATED, 50, true);

	}

// 3.

// ****************************** Complete_UserTask ****************************

// Complete User Task :-

	@CrossOrigin()
	@PostMapping("/completeTaskWithInstanceId/{processInstanceKey}")

	public String completeTaskWithInstanceId(@PathVariable String processInstanceKey, @RequestBody Map variable)

			throws Exception {

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

						System.out.println("Enter ");

					}

					System.out.println("getIds---" + getIds);

					System.out.println("MAp" + activeTaskMap);

					String jobKey = (String) activeTaskMap.get("id");

					System.out.println(jobKey);

					finalJobkey.add(jobKey);

				}

			}

		}

		System.out.println("finalJobkey-----" + finalJobkey);

		String jobKey = (String) finalJobkey.get(0);

		SaasAuthentication sa = new SaasAuthentication("CwDKV~wHD2FcVeHiA_Nvcl.GSVsQdFSf",

				"qY.Cw2iy52u7KSqumTNcjlOKiBnPK_68qpQtDKK_8SpEiG.WthMXAv2~fZIdT9v2");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		Task task = client.completeTask(jobKey, variable);

		return " User Task Completed Successfully ";

	}

// 4.

// ****************************** Get_All_Variable ****************************

// getVariable :-

	@CrossOrigin()
	@GetMapping("/getVariable/{processInstanceKey}")

	public List getVariable(@PathVariable String processInstanceKey) {

		// All Activated Task :-

		// String activeUrl = "http://localhost:8080/getActivedTaskList";

		// particular User or Admin & processName :-

		String activeUrl = "http://localhost:8080/getActivedTaskList";

		ResponseEntity<List> getActiveTaskList = rest.getForEntity(activeUrl, List.class);

		Map mp = new HashMap();

		List finalInsuranceVaiableList = new ArrayList();

		List activeTaskList = getActiveTaskList.getBody();

		for (Object getTraceId : activeTaskList) {

			Map activeTaskMap = (Map) getTraceId;

			List<Object> getVariableList = (List<Object>) activeTaskMap.get("variables");

			for (Object getVariable : getVariableList) {

				Map getVariableMap = (Map) getVariable;

				String getIds = (String) getVariableMap.get("id");

				String[] str = getIds.split("-");

				String stringGetprocessInstanceKey = str[0];

				if (processInstanceKey.equals(stringGetprocessInstanceKey)) {

					System.out.println("Enter");

					System.out.println("getVariableMap" + getVariableMap);

					Object object = getVariableMap.get("value");

					List obj = (List) object;

					for (Object variable : obj) {

						// System.out.println(variable);

						// before code :

						// finalInsuranceVaiableList.add(object);

						finalInsuranceVaiableList.add(variable);

					}

					// System.out.println(object);

					// mp.put("finalList", object);

				}

			}

		}

		return finalInsuranceVaiableList;

	}

	//
	@CrossOrigin
	@GetMapping("/getInvoiceDetails/{userName}")
	public List<InvoiceDetails> getInvoiceDetails(@PathVariable String userName) throws Exception {

//		List<Cart> cartList = new ArrayList<>();
//
//		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddCart.json");
//
//		// Try reading the content of the InputStream
//		try {
//			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {
//			};
//			cartList = objectMapper.readValue(jsonFilePath, typeReference);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// Get the JSON file path
		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddCart.json");
		File file = new File(jsonFilePath);

		// Ensure the file exists or create a new one if necessary
		if (!file.exists()) {
		    file.createNewFile();
		}

		// Initialize the cart list
		List<Cart> cartList = new ArrayList<>();

		// Read and deserialize the JSON content
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
		    TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {};
		    cartList = objectMapper.readValue(fileInputStream, typeReference);
		} catch (IOException e) {
		    e.printStackTrace();
		}


		InvoiceDetails invoiceDetails = new InvoiceDetails();

		BillingTo billingTo = invoiceDetails.getBillingTo();

		// String curruntDate = invoiceDetails.getCurruntDate();

		String invoiceNumber = invoiceDetails.getInvoiceNumber();

		// invoiceDetails.setCurruntDate("22-jun-2023");

		invoiceDetails.setInvoiceNumber("786565454678");

		List userLists = new ArrayList<>();

		for (Cart listFromCart : cartList) {

			String userid = listFromCart.getUserid();

			if (userid.equals(userName)) {

				Map userList = new HashMap<>();

				List<ProductDetailss> productList = listFromCart.getProductList();

				long sum = 0;

				for (ProductDetailss price : productList) {

					long product_Price = price.getProduct_Price();

					sum = sum + product_Price;

					System.out.println(sum);

				}

				int count = listFromCart.getCount();

				userList.put("productList", productList);

				userList.put("count", count);

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

				Date date = new Date();

				System.out.println(date);

				userList.put("curruntDate", date);

				userList.put("invoiceNumber", "786565454678");

				Map billTos = new HashMap<>();

				billTos.put("address", "chennai");

				billTos.put("email", userName + "@gmail.com");

				billTos.put("contactNum", "12345676789");

				billTos.put("address", "chennai");

				userList.put("billingTo", billTos);

				userList.put("subtotal", "786565454678");

				userList.put("discount", "786565454678");

				userList.put("tax", "786565454678");

				userList.put("total", sum);

				userLists.add(userList);

			}

		}

		List<InvoiceDetails> reqBodyMap = objectMapper.convertValue(userLists,

				new TypeReference<List<InvoiceDetails>>() {

				});

		return reqBodyMap;
	}

//	@CrossOrigin()
//	@GetMapping("/getVariable/{processInstanceKey}")

//	public List getVariable(@PathVariable String processInstanceKey) {
//
//		String activeUrl = "http://localhost:8080/getActivedTaskList";
//
//		ResponseEntity<List> getActiveTaskList = rest.getForEntity(activeUrl, List.class);
//
//		Map mp = new HashMap();
//
//		List finalInsuranceVaiableList = new ArrayList();
//
//		List activeTaskList = getActiveTaskList.getBody();
//
//		for (Object getTraceId : activeTaskList) {
//
//			Map activeTaskMap = (Map) getTraceId;
//
//			List<Object> getVariableList = (List<Object>) activeTaskMap.get("variables");
//
//			for (Object getVariable : getVariableList) {
//
//				Map getVariableMap = (Map) getVariable;
//
//				String getIds = (String) getVariableMap.get("id");
//
//				String[] str = getIds.split("-");
//
//				String stringGetprocessInstanceKey = str[0];
//
//				if (processInstanceKey.equals(stringGetprocessInstanceKey)) {
//
//					System.out.println("Enter");
//
//					System.out.println("getVariableMap" + getVariableMap);
//
//					//finalInsuranceVaiableList.add(getVariableMap);
//					
//					Object object = getVariableMap.get("value");
//					System.out.println(object);
//					finalInsuranceVaiableList.add(object);
//					
//					
//
//				}
//
//			}
//
//		}
//
//		return finalInsuranceVaiableList;
//
//	}

// 5.

// ****************************** Payment_Process_Complete

// ****************************

	@CrossOrigin()
	@PostMapping("/paymentProcess")

	public String paymentReceived(@RequestBody Map paymentDetails) {

		System.out.println("PaymentProcess Enter" + paymentDetails);

		String messageName = (String) paymentDetails.get("messageName");

		String key = (String) paymentDetails.get("correlationKey");

		zeebeClient.newPublishMessageCommand().messageName(messageName).correlationKey(key).send().join();

		return "Payment Done Successfully";

	}

// User_Side :-

// Product _Updates or Create :-

// 6.

// ****************************** Create or Updates ****************************

//	@CrossOrigin()
//	@PostMapping("/createProductByAdmin")
//	public List<ProductDetailss> createProductByAdmin(@RequestBody String input) throws Exception {
//		// Convert input JSON string into a Map
//		Map<String, Object> inputValue = objectMapper.readValue(input, Map.class);
//
//		// Use InputStream to read AddProduct.json file
//		InputStream inputStream = orderUtil.getFileInputStream("jsonreader/AddProduct.json");
//		InputStream inputStreamTest = orderUtil.getFileInputStream("jsonreader/CustomerOrderHistory.json");
//
//		String jsonFilePath=orderUtil.readJsonFile("jsonreader/AddProduct.json");
//		
//		List<ProductDetailss> productList = new ArrayList<>();
//		List<ProductDetailss> productListTest = new ArrayList<>();
//
//		// Try reading existing product list from file
//		try {
//			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//			};
//			productList = objectMapper.readValue(jsonFilePath, typeReference);
//			productListTest = objectMapper.readValue(inputStreamTest, typeReference);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// Extract values from the input
//		String inputId = (String) inputValue.get("product_ID");
//		String inputName = (String) inputValue.get("product_Name");
//		int stock = (int) inputValue.get("stock");
//
//		boolean found = false;
//
//		// Iterate through the product list to check if the product exists
//		for (ProductDetailss lst : productList) {
//			String itemId1 = lst.getProduct_ID();
//			String itemName = lst.getProduct_Name();
//
//			// If both product ID and name match, update the stock
//			if (itemId1.equals(inputId) && itemName.equals(inputName)) {
//				lst.setStock(lst.getStock() + stock);
//				found = true;
//				break;
//			}
//
//			// If the product ID matches but the name doesn't, log and set found flag
//			if (itemId1.equals(inputId) && !itemName.equals(inputName)) {
//				System.out.println("Product ID matches but names do not match.");
//				found = true;
//			}
//		}
//
//		// If product wasn't found, add the new product to the list
//		if (!found) {
//			ProductDetailss newProduct = objectMapper.convertValue(inputValue, ProductDetailss.class);
//			productList.add(newProduct);
//		}
//		
//		String jsonFilePath1=orderUtil.readJsonFile("jsonreader/AddProduct.json");
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath1), productList);
//		
//
//		// Return the updated product list
//		return productList;
//	}
	
	
	
	

//7.

// ****************************** EditProductDetailsByID ****************************

//Edit a Product from Add_ProductLIst BY ID :-

// editProductDetailsByID

//	@CrossOrigin()
//	@PostMapping("/editProductDetailsByID")
//
//	public List<ProductDetailss> editProductDetailsByID(@RequestBody String input) throws Exception {
//		System.out.println("Entering" + input);
//
//		// Convert input string to a Map using ObjectMapper
//		Map<String, Object> inputValue = objectMapper.readValue(input, Map.class);
//
//		
//		
//		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddProduct.json");
//
//		// Define a list to hold the ProductDetails objects
//		List<ProductDetailss> productList = new ArrayList<>();
//
//		try {
//			
//
//			// Define the type for ObjectMapper to read a List of ProductDetails
//			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//			};
//
//			// Read the list of products from the InputStream
//			productList = objectMapper.readValue(jsonFilePath, typeReference);
//
//			
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//// my logics :-
//
//		String inputProductId = (String) inputValue.get("product_ID");
//
//// String inputName = (String) inputValue.get("product_Name");
//
//// int stock = (int) inputValue.get("stock");
//
//		List finalList = new ArrayList<>();
//
//		finalList.add(inputValue);
//
//		boolean found = false;
//
//// iterate Products :-
//
//		for (ProductDetailss lst : productList) {
//
//			String productIdFromProductList = lst.getProduct_ID();
//
//			if (inputProductId.equals(productIdFromProductList)) {
//
//// ProductDetailss editObject = new ProductDetailss();
//
//// editObject.getProduct_Name(lst.setProduct_Name(inputProductId));
//
//				String product_Name = (String) inputValue.get("product_Name");
//
//				int product_Price = (int) inputValue.get("product_Price");
//
//				int product_Qnty = (int) inputValue.get("product_Qnty");
//
//				int stock = (int) inputValue.get("stock");
//
//				lst.setProduct_Name(product_Name);
//
//				lst.setProduct_Price(product_Price);
//
//				Long l2 = Long.valueOf(product_Qnty);
//
//				// lst.setProduct_Qnty(l2);
//
//				lst.setStock(stock);
//
//				break;
//
//			}
//
//		}
//
//		Path outputPath = orderUtil.getFilePath("output/AddProduct.json");
//		File outputFile = outputPath.toFile();
//		
//		String jsonFilePath1 = orderUtil.readJsonFile("jsonreader/AddProduct.json");
//
//		
//		
//		
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath1), productList);
//
//		return productList;
//
//	}

// 7.

// ****************************** Delete_Porduct_ByID ****************************

// Delete By ID :-

//	@CrossOrigin()
//	@DeleteMapping("/deleteProductByID/{inputID}")
//	public List<ProductDetailss> deleteProductByID(@PathVariable String inputID) throws Exception {
//
//		// Read the product list from AddProduct.json using InputStream
//		InputStream inputStream = orderUtil.getFileInputStream("jsonreader/AddProduct.json");
//		
//		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddProduct.json");
//
//		List<ProductDetailss> productList = new ArrayList<>();
//
//		// Try reading the existing products from the file
//		try {
//			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//			};
//			productList = objectMapper.readValue(jsonFilePath, typeReference);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//
//		// Iterate through the list to find and remove the product by ID
//		Iterator<ProductDetailss> iterator = productList.iterator();
//		while (iterator.hasNext()) {
//			ProductDetailss product = iterator.next();
//			String product_ID = product.getProduct_ID();
//
//			if (product_ID.equals(inputID)) {
//				System.out.println("Product with ID " + inputID + " found. Deleting...");
//				iterator.remove(); // Remove the product if the ID matches
//			}
//		}
//
//		
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), productList);
//		
//
//		System.out.println("After Deleting By ID: " + productList);
//
//		return productList; // Return the updated product list
//	}

// 8.

// ****************************** Get_All_Product_List****************************

// Product Get All :-

// Getall AddProduct JSON for Admin :-

//	@CrossOrigin()
//	@GetMapping("/getAllProductDeatils")
//	public JSONArray getAllProductDetails() throws Exception {
//
//		
//String jsonFilePath=orderUtil.readJsonFile("jsonreader/AddProduct.json");
//		
//		
//		try {
//	        // Read the JSON file as a String
//	        String content = new String(Files.readAllBytes(new File(jsonFilePath).toPath()));
//	        
//	        System.out.println("content---"+content);
//	        
//	        JSONParser parser = new JSONParser();
//	        Object obj = null;
//			try {
//				obj = parser.parse(content);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        JSONArray jsonArray = (JSONArray) obj;
//	        
//	        return jsonArray;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//		
//		
//
//	}

// 9.

// ****************************** Get_ProductList_ByID ****************************

// getProductDetailsById

//	@CrossOrigin()
//	@GetMapping("/getProductDetailsById/{id}")
//
//	public List getProductDetailsById(@PathVariable String id) throws Exception {
//
//		List finalList = new ArrayList<>();
//
//		List sls = new ArrayList<>();
//
//		String url = "http://localhost:8080/getAllProductDeatils";
//
//		ResponseEntity<List> forEntity = rest.getForEntity(url, List.class);
//
//		List res = forEntity.getBody();
//
//		for (Object proList : res) {
//
//			Map ms = (Map) proList;
//
//			Map proMap = (Map) proList;
//
//			if (id.equals(proMap.get("product_ID"))) {
//
//				Map mpl = new HashMap<>();
//
//				String product_ID = (String) proMap.get("product_ID");
//
//				int product_Qnty = (int) proMap.get("product_Qnty");
//
//				int product_Price = (int) proMap.get("product_Price");
//
//				String product_Name = (String) proMap.get("product_Name");
//
//				int stock = (int) proMap.get("stock");
//
//				mpl.put("product_ID", product_ID);
//
//				mpl.put("product_Qnty", product_Qnty);
//
//				mpl.put("product_Price", product_Price);
//
//				mpl.put("product_Name", product_Name);
//
//				mpl.put("stock", stock);
//
//				finalList.add(mpl);
//
//			}
//
//		}
//		System.out.println(finalList);
//		return finalList;
//
//	}

// ****************************** Cart ******************************

// 10.

// ****************************** Add_A_Product_To_Cart ****************************

//	@CrossOrigin()
//	@GetMapping("/addProductToCart/{userid}/{cartid}/{productID}")
//
//	public List<Cart> addProductToCart(@PathVariable String userid, @PathVariable String cartid,
//			@PathVariable String productID) throws Exception {
//
//// Read available products from AddProduct.json
//		List<ProductDetailss> totalAvailableProduct = new ArrayList<>();
//		InputStream inputStream = orderUtil.getFileInputStream("jsonreader/AddProduct.json");
//		
//		String jsonFilePath=orderUtil.readJsonFile("jsonreader/AddProduct.json");
//
//// Try to read the existing products from the file
//		try {
//			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//			};
//			totalAvailableProduct = objectMapper.readValue(jsonFilePath, typeReference);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//
//// Get the product to be added
//		List<ProductDetailss> getUserList = new ArrayList<>();
//		for (ProductDetailss lst : totalAvailableProduct) {
//			if (productID.equals(lst.getProduct_ID())) {
//				getUserList.add(lst);
//			}
//		}
//
//// Read existing carts from AddProduct.json
//		List<Cart> cartList = new ArrayList<>();
//		inputStream = orderUtil.getFileInputStream("jsonreader/AddCart.json"); // Ensure this points to the correct path
//
//// Try to read the existing carts from the file
//		try {
//			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {
//			};
//			cartList = objectMapper.readValue(jsonFilePath, typeReference);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//
//// Logic to add product to user's cart
//		boolean found = false;
//		for (Cart lst : cartList) {
//			if (userid.equals(lst.getUserid())) {
//				List<ProductDetailss> productList = lst.getProductList();
//
//// Check if product already exists in the cart
//				for (ProductDetailss rs : getUserList) {
//					boolean exists = productList.stream().anyMatch(p -> p.getProduct_ID().equals(rs.getProduct_ID()));
//					if (!exists) {
//						productList.add(rs); // Add the new product if it doesn't exist
//					}
//				}
//				found = true;
//				break; // Exit loop if the user cart is found
//			}
//		}
//
//// Create a new cart if no existing cart was found for the user
//		if (!found) {
//			Cart newCart = new Cart();
//			newCart.setCartid(cartid);
//			newCart.setUserid(userid);
//			newCart.setProductList(getUserList);
//			cartList.add(newCart);
//		}
//
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), cartList);
//// 
//// Prepare the final result to return
//		List<Cart> finalResult = new ArrayList<>();
//		for (Cart view : cartList) {
//			if (userid.equals(view.getUserid())) {
//				finalResult.add(view);
//			}
//		}
//
//		return finalResult;
//	}

// 11.

// ****************************** Remove_A_Product_From_Cart_ByID ****************************

//	@CrossOrigin()
//	@GetMapping("/removeProductFromCart/{userid}/{cartid}/{productID}")
//
//	public List<Cart> removeProductFromCart(@PathVariable String userid, @PathVariable String cartid,
//			@PathVariable String productID) throws Exception {
//// Use getFileInputStream to read the cart data
//		List<Cart> cartList = new ArrayList<>();
//
//		String jsonFilePath=orderUtil.readJsonFile("jsonreader/AddCart.json");
//		TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {
//		};
//		cartList = objectMapper.readValue(jsonFilePath, typeReference);
//		
//		
//// Remove the product from the user's cart
//		for (Cart cart : cartList) {
//			if (userid.equals(cart.getUserid())) {
//				List<ProductDetailss> productList = cart.getProductList();
//// Use iterator for safe removal
//				productList.removeIf(product -> product.getProduct_ID().equals(productID));
//			}
//		}
//
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), cartList);
//
//
//// Prepare the final result to return
//		List<Cart> finalResult = new ArrayList<>();
//		for (Cart cart : cartList) {
//			if (userid.equals(cart.getUserid())) {
//				finalResult.add(cart);
//			}
//		}
//
//		return finalResult;
//	}

	@GetMapping("/getActiveTaskByUser/{adminName}")

	public List<Task> getActiveTaskByUser(@PathVariable String adminName) throws TaskListException {

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		// return client.getTasks(true, TaskState.CREATED, 50);

		// return client.getTasks(true, TaskState.CREATED, 5000, true);

		return client.getAssigneeTasks(adminName, TaskState.CREATED, 50, true);

	}

	//

	@GetMapping("/getAssignedTaskByAssignee/{assigneeName}")

	public List<Task> getAssignedTaskByAssignee(@PathVariable String assigneeName) throws TaskListException {
		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ", // Client ID or Token Key
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf" // Client Secret or Token Secret
		);

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8") // Correct
																										// taskList URL
				.build();

		System.out.println("Client Initialized: " + client);

		List<Task> assigneeTasks = client.getAssigneeTasks(assigneeName, TaskState.CREATED, 50, true);

		System.out.println("Active tasks: " + assigneeTasks);
		return assigneeTasks;

	}

	@CrossOrigin
	@GetMapping("/getAssignedTask/{processName}/{adminName}")

	public List<Task> getAssignedTask(@PathVariable String processName, @PathVariable String adminName)
			throws TaskListException {

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		List<Task> assigneeTasks = client.getAssigneeTasks(adminName, TaskState.CREATED, 50, true);

//		List ls = new ArrayList<>();
//
//		List variable = new ArrayList<>();
//
//		Map mp = new HashMap<>();
//
//		for (Task processNameList : assigneeTasks) {
//
//			String processNames = processNameList.getProcessName();
//
//			if (processNames.equals(processName)) {
//
//				// ls.add(processNameList);
//
//				String id = processNameList.getId();
//
//				String name = processNameList.getName();
//
//				TaskState taskState = processNameList.getTaskState();
//
//				List<Variable> variables = processNameList.getVariables();
//
//				// iterating variable :-
//
//				for (Variable variablesCart : variables) {
//
//					VariableType type = variablesCart.getType();
//
//					String variableType = type.name();
//
//					if (variableType.equals("LIST")) {
//
//						variable.add(variablesCart.getValue());
//
//						mp.put("id", id);
//
//						mp.put("name", name);
//
//						mp.put("taskState", taskState);
//
//						mp.put("variables", variable);
//
//						ls.add(mp);
//					}
//
//				}
//
//			}
//
//		}

		return assigneeTasks;

	}

	@CrossOrigin

	@GetMapping("/getActiveTaskByUserID/{taskId}")

	public Task getvariabletask(@PathVariable String taskId) throws TaskListException {

		System.out.println(taskId);

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		return client.getTask(taskId, true);

	}

	// id

	@CrossOrigin
	@GetMapping("/getAssignedTaskByAssignee/{processName}/{adminName}/{taskID}")

	public List<Task> getAssignedTaskByAssignee(@PathVariable String processName, @PathVariable String adminName,
			@PathVariable String taskID) throws TaskListException {
		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		List<Task> assigneeTasks = client.getAssigneeTasks(adminName, TaskState.CREATED, 50, true);
		List sampleTest = new ArrayList<>();

		for (Task processNameList : assigneeTasks) {

			String processNames = processNameList.getProcessName();

			if (processNames.equals(processName)) {

				String id = processNameList.getId();

				String name = processNameList.getName();

				String processNameExist = processNameList.getProcessName();

				TaskState taskState = processNameList.getTaskState();

				String assignee = processNameList.getAssignee();

				List<Variable> variables = processNameList.getVariables();

				variables.forEach((e) -> {
					if (e.getName().equals("inputVaribale")) {
						Map mp = new HashMap<>();

						mp.put("id", id);

						mp.put("name", name);

						mp.put("processName", processNameExist);

						mp.put("taskState", taskState);

						mp.put("assignee", assignee);

						mp.put("variable", variables);

						sampleTest.add(mp);

					}
				});

			}

		}

		List convertValueTask = objectMapper.convertValue(sampleTest, List.class);

		List lsst = new ArrayList<>();

		List matchList = new ArrayList<>();

		for (Object finalVariableList : convertValueTask) {

			Map mpm = (Map) finalVariableList;

			Object id = mpm.get("id");

			Object name = mpm.get("name");

			Object assignee = mpm.get("assignee");

			Object taskState = mpm.get("taskState");

			Object object2 = mpm.get("variable");

			List lsl = (List) object2;

			for (Object lsstt : lsl) {

				Map getInstanceID = (Map) lsstt;

				String object3 = (String) getInstanceID.get("id");

				String[] str = object3.split("-");

				String version = str[0];

				if (version.equals(taskID)) {

					matchList.add(finalVariableList);

				}

			}

		}

		List instanceIDList = new ArrayList<>();

		for (Object getVariableTypeList : matchList) {

			Map maplist = (Map) getVariableTypeList;

			Object id = maplist.get("id");

			Object name = maplist.get("name");

			Object processNames = maplist.get("processName");

			Object assignee = maplist.get("assignee");

			Object taskState = maplist.get("taskState");

			Object object = maplist.get("variable");

			List matchVariable = (List) object;

			for (Object finalist : matchVariable) {

				Map mpm = (Map) finalist;

				String type = (String) mpm.get("type");

				if (type.equals("LIST")) {

					Map finalMap = new HashMap<>();

					finalMap.put("id", id);

					finalMap.put("name", name);

					finalMap.put("processName", processNames);

					finalMap.put("assignee", assignee);

					finalMap.put("taskState", taskState);

					List variableList = new ArrayList<>();

					variableList.add(finalist);

					finalMap.put("variable", variableList);

					instanceIDList.add(finalMap);

				}

			}

		}

		return sampleTest;

	}

	// Add To Cart API :-
//
//	@CrossOrigin
//
//	@PostMapping("/addToCart")
//
//	public List<Cart> addToCart(@RequestBody Map userCartDetails) throws Exception {
//
//		Map priceFinal = new HashMap<>();
//
//		Map inputValue = objectMapper.convertValue(userCartDetails, Map.class);
//
//		String inputReqproduct_ID = (String) inputValue.get("product_ID");
//
//		String userid = (String) inputValue.get("userid");
//
//		String inputCartid = (String) inputValue.get("cartid");
//
//		int product_Qnty = (int) inputValue.get("product_Qnty");
//
//		File file = new File(
//
//				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");
//
//		if (!file.exists()) {
//
//			file.createNewFile();
//
//		}
//
//		List<ProductDetailss> totalAvailableProduct = new ArrayList<>();
//
//		try {
//
//			FileInputStream fileInputStream = new FileInputStream(file);
//
//			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//
//			};
//
//			totalAvailableProduct = objectMapper.readValue(fileInputStream, typeReference);
//
//			fileInputStream.close();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//
//		List<ProductDetailss> getUserList = new ArrayList<>();
//
//		// for (Object input : inputValue) {
//
//		for (ProductDetailss lst : totalAvailableProduct) {
//
//			String product_ID = lst.getProduct_ID();
//
//			// String s = String.valueOf(input);
//
//			if (product_ID.equals(inputReqproduct_ID)) {
//
//				System.out.println("Enter");
//
//				System.out.println(lst);
//
//				long product_Price2 = lst.getProduct_Price();
//
//				priceFinal.put("priceFinal", product_Price2);
//
//				getUserList.add(lst);
//
//			}
//
//		}
//
//		// }
//
//		File file1 = new File(
//
//				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");
//
//		// Create the file if it doesn't exist
//
//		if (!file1.exists()) {
//
//			file1.createNewFile();
//
//		}
//
//		// Read the existing data from the file into a list
//
//		List<Cart> cartList = new ArrayList<>();
//
//		try {
//
//			FileInputStream fileInputStream = new FileInputStream(file1);
//
//			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {
//
//			};
//
//			cartList = objectMapper.readValue(fileInputStream, typeReference);
//
//			fileInputStream.close();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//
//		boolean found = false;
//
//		List userList = new ArrayList<>();
//
//		Object object = priceFinal.get("priceFinal");
//
//		String string = object.toString();
//
//		System.out.println(object);
//
//		int i = Integer.parseInt(string);
//
//		System.out.println("final price" + i);
//
//		// int p = (int) object;
//
//		// System.out.println("ppp"+p);
//
//		for (Cart lst : cartList) {
//
//			String userID = lst.getUserid();
//
//			String cartid = lst.getCartid();
//
//			if (userid.equals(userID)) {
//
//				List<ProductDetailss> productList = lst.getProductList();
//
//				System.out.println(productList);
//
//				List finalList = new ArrayList<>();
//
//				for (ProductDetailss rs : getUserList) {
//
//					for (ProductDetailss lststr : productList) {
//
//						String product_ID = rs.getProduct_ID();
//
//						String product_ID2 = lststr.getProduct_ID();
//
//						// my part :-
//
//						int product_Qnty2 = lststr.getProduct_Qnty();
//
//						int totalQuantity = product_Qnty2 + product_Qnty;
//
//						System.out.println(totalQuantity);
//
//						if (product_ID.equals(product_ID2)) {
//
//							lststr.setProduct_Qnty(product_Qnty);
//
//							long product_Price2 = lststr.getProduct_Price();
//
//							Long l2 = Long.valueOf(i);
//
//							long prices = l2 * product_Qnty;
//
//							lststr.setProduct_Price(prices);
//
//							found = true;
//
//							break;
//
//						}
//
//					}
//
//					if (cartid.equals(inputCartid)) {
//
//						if (!found) {
//
//							productList.add(rs);
//
//						}
//
//					}
//
//				}
//
//				System.out.println("Final +++" + productList);
//
//				lst.setCount(productList.size());
//
//				found = true;
//
//				break;
//
//			}
//
//		}
//
//		if (!found) {
//
//			Cart newCart = new Cart();
//
//			newCart.setCartid(userid + "_cart");
//
//			newCart.setUserid(userid);
//
//			newCart.setProductList(getUserList);
//
//			cartList.add(newCart);
//
//		}
//
//		try {
//
//			FileWriter fileWriter = new FileWriter(file1);
//
//			objectMapper.writeValue(fileWriter, cartList);
//
//			fileWriter.close();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//
//		List finalResult = new ArrayList<>();
//
//		for (Cart view : cartList) {
//
//			if (userid.equals(view.getUserid())) {
//
//				finalResult.add(view);
//
//			}
//
//		}
//
//		return finalResult;
//
//	}

	// new code : july 7 :-

//	@CrossOrigin
//	@PostMapping("/addToCart")
//	public List<Cart> addToCart(@RequestBody Map<String, Object> userCartDetails) throws Exception {
//		Map<String, Long> priceFinal = new HashMap<>();
//
//		// Convert input map to the appropriate structure
//		Map<String, Object> inputValue = objectMapper.convertValue(userCartDetails, Map.class);
//
//		System.out.println("inputValue----"+inputValue);
//		
//		String inputReqproduct_ID = (String) inputValue.get("product_ID");
//		String userid = (String) inputValue.get("userid");
//		String inputCartid = (String) inputValue.get("cartid");
//		int product_Qnty = (int) inputValue.get("product_Qnty");
//		String product_Name=(String)inputValue.get("product_Name");
////		int product_input_Price=(int)inputValue.get("product_Price");
//
//		List<ProductDetailss> totalAvailableProduct = new ArrayList<>();
//		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddProduct.json");
//		  File file = new File(jsonFilePath);
//		TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {
//		};
//		totalAvailableProduct = objectMapper.readValue(file, typeReference);
//		
//		// Read existing data from AddProduct.json
//		
//	
//
//		// Filter product list based on product_ID
//		List<ProductDetailss> getUserList = new ArrayList<>();
//		for (ProductDetailss product : totalAvailableProduct) {
//			if (product.getProduct_ID().equals(inputReqproduct_ID)) {
//				long product_Price = product.getProduct_Price();
//				priceFinal.put("priceFinal", product_Price);
//				getUserList.add(product);
//			}
//		}
//		
////		List<Cart> cartList = new ArrayList<>();
////		File file1 = new File(jsonFilePath);
////		TypeReference<List<Cart>> typeReference1 = new TypeReference<List<Cart>>() {
////		};
////		cartList = objectMapper.readValue(file1, typeReference1);
//
//		// Read existing data from AddCart.json
//		
//		List<Cart> cartList = new ArrayList<>();
//		try (InputStream inputStream = orderUtil.getFileInputStream("jsonreader/AddCart.json")) {
//			TypeReference<List<Cart>> typeReference1 = new TypeReference<List<Cart>>() {
//			};
//			cartList = objectMapper.readValue(inputStream, typeReference1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		System.out.println("inputReqproduct_ID"+inputReqproduct_ID);
//		System.out.println("userid"+userid);
//		System.out.println("inputCartid"+inputCartid);
//		System.out.println("product_Qnty"+product_Qnty);
//		System.out.println("product_Name"+product_Name);
////		System.out.println("product_input_Price"+product_input_Price);
//		
//		System.out.println("cartList----"+cartList);
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//
//		boolean found = false;
//		long priceValue = priceFinal.getOrDefault("priceFinal", 0L);
//
//		for (Cart cart : cartList) {
//			if (userid.equals(cart.getUserid())) {
//				List<ProductDetailss> productList = cart.getProductList();
//				boolean productFoundInCart = false;
//
//				for (ProductDetailss userProduct : getUserList) {
//					for (ProductDetailss cartProduct : productList) {
//						if (userProduct.getProduct_ID().equals(cartProduct.getProduct_ID())) {
//							cartProduct.setProduct_Qnty(product_Qnty);
//							long totalPrice = priceValue * product_Qnty;
//							cartProduct.setProduct_Price(totalPrice);
//							productFoundInCart = true;
//							break;
//						}
//					}
//					if (!productFoundInCart) {
//						userProduct.setProduct_Qnty(product_Qnty);
//						productList.add(userProduct);
//					}
//				}
//				cart.setCount(productList.size());
//				found = true;
//				break;
//			}
//		}
//
//		// If no matching cart was found, create a new one
//		if (!found) {
//			Cart newCart = new Cart();
//			newCart.setCartid(userid + "_cart");
//			newCart.setUserid(userid);
//			newCart.setProductList(getUserList);
//			newCart.setCount(getUserList.size());
//			cartList.add(newCart);
//		}
//		
//		String jsonFilePath1 = orderUtil.readJsonFile("jsonreader/AddCart.json");
//		
//		 List<Cart> existingCartList;
//		    try {
//		        // Read the existing content into a List<Cart>
//		        existingCartList = objectMapper.readValue(new File(jsonFilePath1), new TypeReference<List<Cart>>() {});
//		    } catch (IOException e) {
//		        throw new Exception("Error reading the JSON file: " + e.getMessage());
//		    }
//
//		    cartList.addAll(existingCartList);
//		    
//		
//		
//		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath1), cartList);
//
//		// Filter cartList to return only the cart for the current user
//		List<Cart> finalResult = new ArrayList<>();
//		for (Cart cart : cartList) {
//			if (userid.equals(cart.getUserid())) {
//				finalResult.add(cart);
//			}
//		}
//
//		return finalResult;
//	}

//	@CrossOrigin
//	@DeleteMapping("/removeFromCart")
//	public List<Cart> removeFromCart(@RequestBody Map<String, Object> input) throws Exception {
//		// Convert input map to required values
//		String inputReqproduct_ID = (String) input.get("product_ID");
//		String userid = (String) input.get("userid");
//		String inputCartid = (String) input.get("cartid"); // Not used in your logic
////		Long product_Qnty = (Long) input.get("product_Qnty"); // Not used in your logic
//
//		
//		// Read the JSON file containing the cart data
//		String jsonFilePath = orderUtil.readJsonFile("jsonreader/AddCart.json");
//
//		// Initialize the ObjectMapper
//		ObjectMapper objectMapper = new ObjectMapper();
//		List<Cart> productList;
//
//		// Read the product list from the JSON file
//		try {
//			productList = objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<Cart>>() {
//			});
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new Exception("Error reading cart data from JSON file.");
//		}
//
//		// Process to remove the product from the user's cart
//		productList.removeIf(cart -> cart.getUserid().equals(userid)
//				&& cart.getProductList().removeIf(product -> product.getProduct_ID().equals(inputReqproduct_ID)));
//
//		// Write the updated product list back to the JSON file
//		try {
//			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), productList);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new Exception("Error writing updated cart data to JSON file.");
//		}
//
//		// Prepare the final result
//		List<Cart> finalResult = productList.stream().filter(cart -> userid.equals(cart.getUserid()))
//				.collect(Collectors.toList());
//
//		return finalResult;
//	}


	// order history
	@GetMapping("/completeAPI/{userName}")

	public List<Task> completeAPI(@PathVariable String userName) throws TaskListException {

		// System.out.println(taskId);

		SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",

				"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		// return client.getAssigneeTasks(userName, TaskState.COMPLETED, 50, true);

		List<Task> assigneeTasks = client.getAssigneeTasks(userName, TaskState.COMPLETED, 50, true);

		List finalList = new ArrayList<>();

		List variableLists = new ArrayList<>();

		for (Task orderHistoryList : assigneeTasks) {

			Map mp = new HashMap<>();

			TaskState taskState = orderHistoryList.getTaskState();

			String creationTime = orderHistoryList.getCreationTime();

			mp.put("taskState", taskState);

			mp.put("creationTime", creationTime);

			List<Variable> variables = orderHistoryList.getVariables();

			for (Variable variableList : variables) {

				VariableType type = variableList.getType();

				String name = type.name();

				if (name.equals("LIST")) {

					Variable variableList2 = variableList;

					// variableLists.add(variableList2);

					// variableLists.add(mp);

					Map mpm = new HashMap<>();

					mpm.put("variables", variableList2);

					mpm.put("taskState", taskState);

					mpm.put("creationTime", creationTime);

					finalList.add(mpm);

				}

			}

		}

		return finalList;

	}

	// orderhistorybyinstanceidnew
//	@GetMapping("/orderHistory/{assingneeName}/{processName}/{instanceId}")
//
//	public List orderHistory1(@PathVariable String assingneeName, @PathVariable String processName,

//	@PathVariable String instanceId) throws Exception {
//
//	int calculation = 0, sum = 0;
//
//	File file = new File(
//
//	"D:\Workspace\Backend\Order_Fullfillment_Cloud_Version\src\main\resources\jsonreader\CustomerOrderHistory.json");
//
//	 
//
//	// Create the file if it doesn't exist
//
//	if (!file.exists()) {
//
//	file.createNewFile();
//
//	}
//
//	// Read the existing data from the file into a list
//
//	List productList = new ArrayList<>();
//
//	try {
//
//	FileInputStream fileInputStream = new FileInputStream(file);
//
//	TypeReference<List> typeReference = new TypeReference<List>() {
//
//	};
//
//	productList = objectMapper.readValue(fileInputStream, typeReference);
//
//	fileInputStream.close();
//
//	} catch (IOException e) {
//
//	e.printStackTrace();
//
//	}
//
//	 
//
//	SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",
//
//	"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");
//
//	 
//
//	CamundaTaskListClient client = new CamundaTaskListClient.Builder()
//
//	.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
//
//	.shouldReturnVariables().authentication(sa).build();
//
//	 
//
//	List<Task> assigneeTasks = client.getTasks(true, TaskState.CREATED, 50, true);
//
//	 
//
//	List filterByAssigneeList = new ArrayList();
//
//	 
//
//	for (Task getprocessNameList : assigneeTasks) {
//
//	String assignee = getprocessNameList.getAssignee();
//
//	if (assingneeName.equals(assignee)) {
//
//	String processNam = getprocessNameList.getProcessName();
//
//	 
//
//	if (processName.equals(processNam)) {
//
//	List<Variable> variables = getprocessNameList.getVariables();
//
//	List variableList = new ArrayList<>();
//
//	for (Variable getVariableList : variables) {
//
//	 
//
//	VariableType type = getVariableList.getType();
//
//	String name = type.name();
//
//	 
//
//	if (name.equals("LIST")) {
//
//	Object value = getVariableList.getValue();
//
//	List lst = (List) value;
//
//	 
//
//	for (Object reference : lst) {
//
//	Map mp = (Map) reference;
//
//	Object object = mp.get("product_Price");
//
//	System.out.println(object);
//
//	int i = (int) object;
//
//	System.out.println("i=" + i);
//
//	sum = sum + i;
//
//	// System.out.println("Total Price is"+sum);
//
//	}
//
//	System.out.println("This is the Value:" + sum);
//
//	 
//
//	}
//
//	 
//
//	String id = getVariableList.getId();
//
//	 
//
//	String[] str = id.split("-");
//
//	 
//
//	String getId = str[0];
//
//	if (instanceId.equals(getId)) {
//
//	Map ml = new HashMap<>();
//
//	variableList.add(getVariableList);
//
//	String id2 = getprocessNameList.getId();
//
//	String creationTime = getprocessNameList.getCreationTime();
//
//	DateFormat formatter6 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//	Date date6 = formatter6.parse(creationTime);
//
//	String str1 = formatter6.format(date6);
//
//	System.out.println(str1);
//
//	TaskState taskState = getprocessNameList.getTaskState();
//
//	String state = "Checking Stock Availability";
//
//	List<Variable> variables2 = getprocessNameList.getVariables();
//
//	 
//
//	ml.put("id", id2);
//
//	ml.put("creationTime", str1);
//
//	ml.put("taskState", state);
//
//	ml.put("variables", variableList);
//
//	ml.put("totalPrice", sum);
//
//	filterByAssigneeList.add(ml);
//
//	 
//
//	}
//
//	}
//
//	 
//
//	}
//
//	 
//
//	}
//
//	 
//
//	}
//
//	 
//
//	// my logics :-
//
//	try {
//
//	 
//
//	FileWriter fileWriter = new FileWriter(file);
//
//	objectMapper.writeValue(fileWriter, filterByAssigneeList);
//
//	fileWriter.close();
//
//	} catch (IOException e) {
//
//	e.printStackTrace();
//
//	}
//
//	 
//
//	return filterByAssigneeList;
//
//	}

	// orderHistory
//	@CrossOrigin()
//	@GetMapping("/orderHistory/{assingneeName}/{processName}/{instanceId}")
//
//	public JSONArray orderHistory(@PathVariable String assingneeName, @PathVariable String processName,
//
//			@PathVariable String instanceId) throws Exception {
//
//		
//		
//		
//		String jsonFilePath=orderUtil.readJsonFile("jsonreader/CustomerOrderHistory.json");
//
//		try {
//	        // Read the JSON file as a String
//	        String content = new String(Files.readAllBytes(new File(jsonFilePath).toPath()));
//	        
//	        System.out.println("content---"+content);
//	        
//	        JSONParser parser = new JSONParser();
//	        Object obj = null;
//			try {
//				obj = parser.parse(content);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        JSONArray jsonArray = (JSONArray) obj;
//	        
//	        return jsonArray;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//
//	}

	// OrderCancelled API :-
	@CrossOrigin()
	@GetMapping("/orderCancelled")

	public Map orderCancelled() throws Exception {

		System.out.println("Enter Contoller");

		Map mp = new HashMap<>();

		String messageName = "OrderCancelled";

		String key = "123";

		mp.put("messageName", messageName);

		mp.put("key", key);

		zeebeClient.newPublishMessageCommand().messageName(messageName).correlationKey(key).send().join();

		return mp;

	}

	@CrossOrigin()
	@GetMapping("/cancelOrderByUser")
	public ResponseEntity<String> cancelOrder() {
		System.out.println("cancelOrderByUser Enter");

		String messageName = "OrderCancelled";
		String key = "123";

		zeebeClient.newPublishMessageCommand().messageName(messageName).correlationKey(key).send().join();

		String successMessage = "Your order has been successfully canceled.";
		return ResponseEntity.ok(successMessage);
	}

	// Get AddToCart :
	@CrossOrigin

	@GetMapping("/getAddToCartDetail/{userName}")
	public List getAddToCartDetail(@PathVariable String userName) throws Exception {
		
		
		List<Cart> addCartList = new ArrayList<>();
		
		String jsonFilePath=orderUtil.readJsonFile("jsonreader/AddCart.json");
		
		 String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

		    // Parse the JSON content to List<Cart>
		    TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {};
		    addCartList = objectMapper.readValue(jsonContent, typeReference);

		
		
	
		List cartListUser = new ArrayList<>();

		for (Cart cartList : addCartList) {

			String userid = cartList.getUserid();

			if (userid.equals(userName)) {

				List<ProductDetailss> productList = cartList.getProductList();

				for (ProductDetailss ref : productList) {

					String product_ID = ref.getProduct_ID();

					String product_Name = ref.getProduct_Name();

					long product_Price = ref.getProduct_Price();

					int product_Qnty = ref.getProduct_Qnty();

					Map mp = new HashMap<>();

					mp.put("product_ID", product_ID);

					mp.put("product_Name", product_Name);

					mp.put("product_Price", product_Price);

					mp.put("product_Qnty", product_Qnty);

					cartListUser.add(mp);

				}

			}

		}

		return cartListUser;

	}
}