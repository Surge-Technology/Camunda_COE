
package com.camunda.orderfullfillment.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camunda.orderfullfillment.model.BillingTo;
import com.camunda.orderfullfillment.model.Cart;
import com.camunda.orderfullfillment.model.InvoiceDetails;
import com.camunda.orderfullfillment.model.ProductDetailss;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Autowired

	ZeebeClient zeebeClient;

	@Autowired

	ProductDetailss ds;

	ObjectMapper objectMapper = new ObjectMapper();

	final RestTemplate rest = new RestTemplate();

	@GetMapping("/test")

	public String demo() {

		return "Working";

	}

// All API's :

// OrderFulfilment API :-

// 1.

// ****************************** Start_WorkFlow_API's ****************************

	@CrossOrigin()

	@PostMapping(value = "/submitProductList")

	public Map<String, Object> startWorkflow(@RequestBody String reqBody) throws TaskListException {

		Map inputVaribale = new HashMap<>();

		System.out.println("Input 1 :-" + reqBody);

		SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",

				"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		try {

			List<ProductDetailss> reqBodyMap = objectMapper.readValue(reqBody,
					new TypeReference<List<ProductDetailss>>() {

					});

			inputVaribale.put("inputVaribale", reqBodyMap);

		} catch (JsonProcessingException e) {

			e.printStackTrace();

		}

		ProcessInstanceEvent processInstEvnt = zeebeClient.newCreateInstanceCommand().bpmnProcessId("OrderFullfillment")

				.latestVersion().variables(inputVaribale).send().join();

		long pID = processInstEvnt.getProcessInstanceKey();

		long processDefinitionKey = processInstEvnt.getProcessDefinitionKey();

		System.out.println("processDefinitionKey" + processDefinitionKey);

		System.out.println("pId : " + pID);

		System.out.println("flow started");

		Map<String, Object> processIdMap = new HashMap<String, Object>();

		processIdMap.put("processInstanceKey", pID);

		return processIdMap;

	}

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

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");
		if (!file.exists()) {

			file.createNewFile();

		}

		List<Cart> cartList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			cartList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

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

	@CrossOrigin()
	@PostMapping("/createProductByAdmin")

	public List<ProductDetailss> createProductByAdmin(@RequestBody String input) throws Exception {

		Map inputValue = objectMapper.readValue(input, Map.class);

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");

// Create the file if it doesn't exist

		if (!file.exists()) {

			file.createNewFile();

		}

// Read the existing data from the file into a list

		List<ProductDetailss> productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

// my logics :-

		String inputId = (String) inputValue.get("product_ID");

		String inputName = (String) inputValue.get("product_Name");

		int stock = (int) inputValue.get("stock");

		List finalList = new ArrayList<>();

		finalList.add(inputValue);

		boolean found = false;

		for (ProductDetailss lst : productList) {

			String itemId1 = lst.getProduct_ID();

			String itemName = lst.getProduct_Name();

			if (itemId1.equals(inputId) && itemName.equals(inputName)) {

				int stock1 = lst.getStock() + stock;

				for (ProductDetailss obj : productList) {

					if (obj.getProduct_ID() == itemId1) {

						obj.setStock(stock1);

						break; // Stop iterating after updating the desired object

					}

				}

				found = true;

				break;

			}

			if (itemId1.equals(inputId) && !itemName.equals(inputName)) {

				System.out.println("not equls");

				found = true;

			}

		}

		if (!found) {

			productList.addAll(finalList);

		}

		try {

			System.out.println("Final List ---->" + productList);

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, productList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return productList;

	}

//7.

// ****************************** EditProductDetailsByID ****************************

//Edit a Product from Add_ProductLIst BY ID :-

// editProductDetailsByID

	@CrossOrigin()
	@PostMapping("/editProductDetailsByID")

	public List<ProductDetailss> editProductDetailsByID(@RequestBody String input) throws Exception {
		System.out.println("Entering" + input);

		Map inputValue = objectMapper.readValue(input, Map.class);

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");

		if (!file.exists()) {

			file.createNewFile();

		}

// Read the existing data from the file into a list

		List<ProductDetailss> productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

// my logics :-

		String inputProductId = (String) inputValue.get("product_ID");

// String inputName = (String) inputValue.get("product_Name");

// int stock = (int) inputValue.get("stock");

		List finalList = new ArrayList<>();

		finalList.add(inputValue);

		boolean found = false;

// iterate Products :-

		for (ProductDetailss lst : productList) {

			String productIdFromProductList = lst.getProduct_ID();

			if (inputProductId.equals(productIdFromProductList)) {

// ProductDetailss editObject = new ProductDetailss();

// editObject.getProduct_Name(lst.setProduct_Name(inputProductId));

				String product_Name = (String) inputValue.get("product_Name");

				int product_Price = (int) inputValue.get("product_Price");

				int product_Qnty = (int) inputValue.get("product_Qnty");

				int stock = (int) inputValue.get("stock");

				lst.setProduct_Name(product_Name);

				lst.setProduct_Price(product_Price);

				Long l2 = Long.valueOf(product_Qnty);

				// lst.setProduct_Qnty(l2);

				lst.setStock(stock);

				break;

			}

		}

		try {

			System.out.println("Final List ---->" + productList);

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, productList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return productList;

	}

// 7.

// ****************************** Delete_Porduct_ByID ****************************

// Delete By ID :-

	@CrossOrigin()
	@DeleteMapping("/deleteProductByID/{inputID}")

	public List<ProductDetailss> deleteProductByID(@PathVariable String inputID) throws Exception {

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");

		if (!file.exists()) {

			file.createNewFile();

		}

		List<ProductDetailss> productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		Iterator<ProductDetailss> iterator = productList.iterator();

		while (iterator.hasNext()) {

			ProductDetailss number = iterator.next();

			String product_ID = number.getProduct_ID();

// String child = ref1.getProduct_ID();

			if (product_ID.equals(inputID)) {

				System.out.println("If condition entered");

// productList.remove(pro);

				iterator.remove();

			}

		}

		try {

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, productList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		System.out.println("After Deleting By ID" + productList);

		return productList;

	}

// 8.

// ****************************** Get_All_Product_List****************************

// Product Get All :-

// Getall AddProduct JSON for Admin :-

	@CrossOrigin()
	@GetMapping("/getAllProductDeatils")

	public JSONArray getAllProductDeatils() {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(

					"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json"));

			JSONArray jsonObject = (JSONArray) obj;

			return jsonObject;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

// 9.

// ****************************** Get_ProductList_ByID ****************************

// getProductDetailsById

	@CrossOrigin()
	@GetMapping("/getProductDetailsById/{id}")

	public List getProductDetailsById(@PathVariable String id) throws Exception {

		List finalList = new ArrayList<>();

		List sls = new ArrayList<>();

		String url = "http://localhost:8080/getAllProductDeatils";

		ResponseEntity<List> forEntity = rest.getForEntity(url, List.class);

		List res = forEntity.getBody();

		for (Object proList : res) {

			Map ms = (Map) proList;

			Map proMap = (Map) proList;

			if (id.equals(proMap.get("product_ID"))) {

				Map mpl = new HashMap<>();

				String product_ID = (String) proMap.get("product_ID");

				int product_Qnty = (int) proMap.get("product_Qnty");

				int product_Price = (int) proMap.get("product_Price");

				String product_Name = (String) proMap.get("product_Name");

				int stock = (int) proMap.get("stock");

				mpl.put("product_ID", product_ID);

				mpl.put("product_Qnty", product_Qnty);

				mpl.put("product_Price", product_Price);

				mpl.put("product_Name", product_Name);

				mpl.put("stock", stock);

				finalList.add(mpl);

			}

		}
		System.out.println(finalList);
		return finalList;

	}

// ****************************** Cart ******************************

// 10.

// ****************************** Add_A_Product_To_Cart ****************************

	@CrossOrigin()
	@GetMapping("/addProductToCart/{userid}/{cartid}/{productID}")

	public List addProductToCart(@PathVariable String userid, @PathVariable String cartid,
			@PathVariable String productID

// @RequestBody String userProductList

	) throws Exception {

// List inputValue = objectMapper.readValue(userProductList, List.class);

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");

// Create the file if it doesn't exist

		if (!file.exists()) {

			file.createNewFile();

		}

// Read the existing data from the file into a list

		List<ProductDetailss> totalAvailableProduct = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {

			};

			totalAvailableProduct = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List<ProductDetailss> getUserList = new ArrayList<>();

// for (Object input : inputValue) {

		for (ProductDetailss lst : totalAvailableProduct) {

			String product_ID = lst.getProduct_ID();

// String s = String.valueOf(input);

			if (productID.equals(product_ID)) {

				System.out.println("Enter");

				System.out.println(lst);

				getUserList.add(lst);

			}

		}

// }

		File file1 = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");

// Create the file if it doesn't exist

		if (!file1.exists()) {

			file1.createNewFile();

		}

// Read the existing data from the file into a list

		List<Cart> cartList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file1);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			cartList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (

		IOException e) {

			e.printStackTrace();

		}

		boolean found = false;

		List userList = new ArrayList<>();

		for (Cart lst : cartList) {

			String userID = lst.getUserid();

			if (userid.equals(userID)) {

				List<ProductDetailss> productList = lst.getProductList();

				System.out.println(productList);

				List finalList = new ArrayList<>();

				for (ProductDetailss rs : getUserList) {

					for (ProductDetailss lststr : productList) {

						String product_ID = rs.getProduct_ID();

						String product_ID2 = lststr.getProduct_ID();

						if (product_ID.equals(product_ID2)) {

							found = true;

						}

					}

					if (!found) {

						productList.add(rs);

					}

				}

				System.out.println("Final +++" + productList);

// lst.setProductList(getUserList);

				found = true;

				break;

			}

		}

		if (!found) {

			Cart newCart = new Cart();

			newCart.setCartid(cartid);

			newCart.setUserid(userid);

			newCart.setProductList(getUserList);

			cartList.add(newCart);

		}

		try {

			FileWriter fileWriter = new FileWriter(file1);

			objectMapper.writeValue(fileWriter, cartList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List finalResult = new ArrayList<>();

		for (Cart view : cartList) {

			if (userid.equals(view.getUserid())) {

				finalResult.add(view);

			}

		}

		return finalResult;

	}

// 11.

// ****************************** Remove_A_Product_From_Cart_ByID ****************************

	@CrossOrigin()
	@GetMapping("/removeProductFromCart/{userid}/{cartid}/{productID}")

	public List<Cart> removeProductFromCart(@PathVariable String userid, @PathVariable String cartid,
			@PathVariable String productID)

			throws Exception {

// System.out.println(inputID);

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");

		if (!file.exists()) {

			file.createNewFile();

		}

		List<Cart> productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		for (Cart ref : productList) {

			if (ref.getUserid().equals(userid)) {

				List<ProductDetailss> productList2 = ref.getProductList();

				Iterator<ProductDetailss> iterator = productList2.iterator();

				while (iterator.hasNext()) {

					ProductDetailss number = iterator.next();

					if (number.getProduct_ID().equals(productID)) {

						iterator.remove();

					}

					System.out.println(productList);

				}

			}

		}

		try {

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, productList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List finalResult = new ArrayList<>();

		for (Cart view : productList) {

			if (userid.equals(view.getUserid())) {

				finalResult.add(view);

			}

		}

		return finalResult;

	}

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

	@CrossOrigin
	@GetMapping("/getAssignedTask/{processName}/{adminName}")

	public List<Task> getAssignedTask(@PathVariable String processName, @PathVariable String adminName)
			throws TaskListException {

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		List<Task> assigneeTasks = client.getAssigneeTasks(adminName, TaskState.CREATED, 50, true);

		List ls = new ArrayList<>();

		List variable = new ArrayList<>();

		Map mp = new HashMap<>();

		for (Task processNameList : assigneeTasks) {

			String processNames = processNameList.getProcessName();

			if (processNames.equals(processName)) {

				// ls.add(processNameList);

				String id = processNameList.getId();

				String name = processNameList.getName();

				TaskState taskState = processNameList.getTaskState();

				List<Variable> variables = processNameList.getVariables();

				// iterating variable :-

				for (Variable variablesCart : variables) {

					VariableType type = variablesCart.getType();

					String variableType = type.name();

					if (variableType.equals("LIST")) {

						variable.add(variablesCart.getValue());

						mp.put("id", id);

						mp.put("name", name);

						mp.put("taskState", taskState);

						mp.put("variables", variable);

						ls.add(mp);
					}

				}

			}

		}

		return ls;

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

				Map mp = new HashMap<>();

				mp.put("id", id);

				mp.put("name", name);

				mp.put("processName", processNameExist);

				mp.put("taskState", taskState);

				mp.put("assignee", assignee);

				mp.put("variable", variables);

				sampleTest.add(mp);

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

		return instanceIDList;

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

	@CrossOrigin
	@PostMapping("/addToCart")

	public List<Cart> addToCart(@RequestBody Map userCartDetails) throws Exception {

		Map priceFinal = new HashMap<>();

		Map inputValue = objectMapper.convertValue(userCartDetails, Map.class);

		String inputReqproduct_ID = (String) inputValue.get("product_ID");

		String userid = (String) inputValue.get("userid");

		String inputCartid = (String) inputValue.get("cartid");

		int product_Qnty = (int) inputValue.get("product_Qnty");

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddProduct.json");

		// Create the file if it doesn't exist

		if (!file.exists()) {

			file.createNewFile();

		}

		// Read the existing data from the file into a list

		List<ProductDetailss> totalAvailableProduct = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<ProductDetailss>> typeReference = new TypeReference<List<ProductDetailss>>() {

			};

			totalAvailableProduct = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List<ProductDetailss> getUserList = new ArrayList<>();

		for (ProductDetailss lst : totalAvailableProduct) {

			String product_ID = lst.getProduct_ID();

			if (product_ID.equals(inputReqproduct_ID)) {

				long product_Price2 = lst.getProduct_Price();

				priceFinal.put("priceFinal", product_Price2);

				getUserList.add(lst);

			}

		}

		File file1 = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");

		// Create the file if it doesn't exist

		if (!file1.exists()) {

			file1.createNewFile();

		}

		// Read the existing data from the file into a list

		List<Cart> cartList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file1);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			cartList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		boolean found = false;

		List userList = new ArrayList<>();

		Object object = priceFinal.get("priceFinal");

		String string = object.toString();

		int i = Integer.parseInt(string);

		for (Cart lst : cartList) {

			String userID = lst.getUserid();

			String cartid = lst.getCartid();

			if (userid.equals(userID)) {

				List<ProductDetailss> productList = lst.getProductList();

				List finalList = new ArrayList<>();

				for (ProductDetailss rs : getUserList) {

					for (ProductDetailss lststr : productList) {

						String product_ID = rs.getProduct_ID();

						String product_ID2 = lststr.getProduct_ID();

						// my part :-

						int product_Qnty2 = lststr.getProduct_Qnty();

						int totalQuantity = product_Qnty2 + product_Qnty;

						if (product_ID.equals(product_ID2)) {

							lststr.setProduct_Qnty(product_Qnty);

							long product_Price2 = lststr.getProduct_Price();

							Long l2 = Long.valueOf(i);

							long prices = l2 * product_Qnty;

							lststr.setProduct_Price(prices);

							found = true;

							break;

						}

					}

					if (cartid.equals(inputCartid)) {

						if (!found) {

							rs.setProduct_Qnty(product_Qnty);

							productList.add(rs);

						}

					}

				}

				lst.setCount(productList.size());

				found = true;

				break;

			}

		}

		if (!found) {

			Cart newCart = new Cart();

			newCart.setCartid(userid + "_cart");

			newCart.setUserid(userid);

			newCart.setProductList(getUserList);

			newCart.setCount(getUserList.size());

			cartList.add(newCart);

		}

		try {

			FileWriter fileWriter = new FileWriter(file1);

			objectMapper.writeValue(fileWriter, cartList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List finalResult = new ArrayList<>();

		for (Cart view : cartList) {

			if (userid.equals(view.getUserid())) {

				finalResult.add(view);

			}

		}

		return finalResult;

	}

	// Remove From Car api :-

	@CrossOrigin

	@DeleteMapping("/removeFromCart")

	public List<Cart> removeFromCart(@RequestBody Map input) throws Exception {

		Map inputValue = objectMapper.convertValue(input, Map.class);

		String inputReqproduct_ID = (String) inputValue.get("product_ID");

		String userid = (String) inputValue.get("userid");

		String inputCartid = (String) inputValue.get("cartid");

		int product_Qnty = (int) inputValue.get("product_Qnty");

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");

		if (!file.exists()) {

			file.createNewFile();

		}

		List<Cart> productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		for (Cart ref : productList) {

			if (ref.getUserid().equals(userid)) {

				List<ProductDetailss> productList2 = ref.getProductList();

				Iterator<ProductDetailss> iterator = productList2.iterator();

				while (iterator.hasNext()) {

					ProductDetailss number = iterator.next();

					if (number.getProduct_ID().equals(inputReqproduct_ID)) {

						iterator.remove();

					}

					System.out.println(productList);

				}

			}

		}

		try {

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, productList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		List finalResult = new ArrayList<>();

		for (Cart view : productList) {

			if (userid.equals(view.getUserid())) {

				finalResult.add(view);

			}

		}

		return finalResult;

	}

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
	@CrossOrigin()
	@GetMapping("/orderHistory/{assingneeName}/{processName}/{instanceId}")

	public List orderHistory(@PathVariable String assingneeName, @PathVariable String processName,

			@PathVariable String instanceId) throws Exception {

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\	.json");

		// Create the file if it doesn't exist

		if (!file.exists()) {

			file.createNewFile();

		}

		// Read the existing data from the file into a list

		List productList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List> typeReference = new TypeReference<List>() {

			};

			productList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",

				"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		List<Task> assigneeTasks = client.getTasks(true, TaskState.CREATED, 50, true);

		List sizelist = new ArrayList<>();

		List filterByAssigneeList = new ArrayList();

		int size = assigneeTasks.size();

		for (Task getprocessNameList : assigneeTasks) {

			String assignee = getprocessNameList.getAssignee();

			if (assingneeName.equals(assignee)) {

				String processNam = getprocessNameList.getProcessName();

				if (processName.equals(processNam)) {

					List<Variable> variables = getprocessNameList.getVariables();

					List variableList = new ArrayList<>();

					List totalPriceAmount = new ArrayList<>();

					// Map mpTotal = new HashMap<>();

					int size1 = variables.size();

					for (Variable getVariableList : variables) {

						String id = getVariableList.getId();

						String[] str = id.split("-");

						String getId = str[0];

						if (instanceId.equals(getId)) {

							int sum = 0;

							sizelist.add(getprocessNameList);

							int size2 = sizelist.size();

							VariableType type = getVariableList.getType();

							String name = type.name();

							if (name.equals("LIST")) {

								Object value = getVariableList.getValue();

								List lst = (List) value;

								for (Object reference : lst) {

									Map mp = (Map) reference;

									Object object = mp.get("product_Price");

									int i = (int) object;

									sum = sum + i;

								}

							}

							Map ml = new HashMap<>();

							variableList.add(getVariableList);

							String id2 = getprocessNameList.getId();

							String creationTime = getprocessNameList.getCreationTime();

							DateFormat formatter6 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

							Date date6 = formatter6.parse(creationTime);

							String str1 = formatter6.format(date6);

							TaskState taskState = getprocessNameList.getTaskState();

							String state = "Checking Stock Availability";

							List<Variable> variables2 = getprocessNameList.getVariables();

							ml.put("id", id2);

							ml.put("creationTime", str1);

							ml.put("taskState", state);

							ml.put("variables", variableList);

							ml.put("totalPrice", sum);

							filterByAssigneeList.add(ml);

						}

					}

				}

			}

		}

		// my logics :-

		try {

			FileWriter fileWriter = new FileWriter(file);

			objectMapper.writeValue(fileWriter, filterByAssigneeList);

			fileWriter.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return filterByAssigneeList;

	}

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

	// Get AddToCart :
	@CrossOrigin

	@GetMapping("/getAddToCartDetail/{userName}")

	public List getAddToCartDetail(@PathVariable String userName) throws Exception {

		File file = new File(

				"D:\\Workspace\\Backend\\Order_Fullfillment_Cloud_Version\\src\\main\\resources\\jsonreader\\AddCart.json");

		// Create the file if it doesn't exist

		if (!file.exists()) {

			file.createNewFile();

		}

		// Read the existing data from the file into a list

		List<Cart> addCartList = new ArrayList<>();

		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			TypeReference<List<Cart>> typeReference = new TypeReference<List<Cart>>() {

			};

			addCartList = objectMapper.readValue(fileInputStream, typeReference);

			fileInputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

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