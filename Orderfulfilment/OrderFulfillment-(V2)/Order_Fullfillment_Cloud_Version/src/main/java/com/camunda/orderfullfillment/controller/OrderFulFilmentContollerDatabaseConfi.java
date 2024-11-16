package com.camunda.orderfullfillment.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camunda.orderfullfillment.Entity.AddCart;
import com.camunda.orderfullfillment.Entity.OrderDetail;
import com.camunda.orderfullfillment.Entity.ProductDetail;
import com.camunda.orderfullfillment.Entity.UserMaster;
import com.camunda.orderfullfillment.Rep.CardRep;
import com.camunda.orderfullfillment.Rep.OrderRep;
import com.camunda.orderfullfillment.Rep.ProductRep;
import com.camunda.orderfullfillment.Rep.UserMasterRep;
import com.camunda.orderfullfillment.model.Cart;
import com.camunda.orderfullfillment.model.ProductDetailss;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

@RestController
@RequestMapping("/api")
public class OrderFulFilmentContollerDatabaseConfi {
	
	@Autowired
	ProductRep productRep;

	@Autowired
	UserMasterRep userRep;

	@Autowired
	CardRep cardRep;
	
	@Autowired
	OrderRep orderRep;
	
	@Autowired

	ZeebeClient zeebeClient;
	
	private AtomicLong orderIdCounter = new AtomicLong(1);

	ObjectMapper objectMapper = new ObjectMapper();
	
	final RestTemplate rest = new RestTemplate();
	
	private static final String csvFilePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "list.csv").toString();

	// ------------Create Product-----------------------//

	@CrossOrigin
	@PostMapping("/createProductByAdmin")
	public ProductDetail createProductByAdmin(@RequestBody String input) throws Exception {

		JSONObject productJson = new JSONObject(input);
System.out.println("---"+productJson);
		ProductDetail obj = new ProductDetail();

		obj.setProductName(productJson.getString("product_Name"));
		obj.setProductPrice(productJson.getDouble("product_Price"));
		obj.setProductQuantity(productJson.getInt("product_Qnty"));
		obj.setStock(productJson.getInt("stock"));

		return productRep.save(obj);
	}
	
	
	//-------Show the Edit Product------//
	
	
	@CrossOrigin()
	@GetMapping("/getProductDetailsById/{id}")

	public List getProductDetailsById(@PathVariable String id) throws Exception {

		System.out.println("id---" + id);

		String productId = id;
		Long UnquieId = Long.parseLong(productId);

		List<Map<String, Object>> finalList = new ArrayList<>();

		String url = "http://localhost:8080/api/getAllProductDeatils";
		ResponseEntity<List> forEntity = rest.getForEntity(url, List.class);
		List<Map<String, Object>> res = (List<Map<String, Object>>) forEntity.getBody();

		for (Map<String, Object> proMap : res) {
		    System.out.println("proMap--" + proMap.get("productId"));
		    System.out.println("id---" + UnquieId);

		    // Convert proMap.get("productId") to Long for comparison
		    Long productIdInMap = proMap.get("productId") != null ? Long.parseLong(proMap.get("productId").toString()) : null;

		    if (UnquieId.equals(productIdInMap)) {
		        Map<String, Object> mpl = new HashMap<>();
		        mpl.put("product_ID", proMap.get("productId"));
		        mpl.put("product_Qnty", proMap.get("productQuantity"));
		        mpl.put("product_Price", proMap.get("productPrice"));
		        mpl.put("product_Name", proMap.get("productName"));
		        mpl.put("stock", proMap.get("stock"));

		        finalList.add(mpl);
		    }
		}

		System.out.println("----"+finalList);
		return finalList;

	}
	
	

	// ---------------Edit the Product--------------------//

	@CrossOrigin()
	@PostMapping("/editProductDetailsByID")

	public ProductDetail editProductDetailsByID(@RequestBody String input) throws Exception {

		JSONObject productJson = new JSONObject(input);
		System.out.println(productJson.getLong("product_ID"));
		ProductDetail existData = productRep.findById(productJson.getLong("product_ID")).get();
		System.out.println(existData.getProductName());
		System.out.println(existData.getProductPrice());
		existData.setProductId(productJson.getLong("product_ID"));
		existData.setProductName(productJson.getString("product_Name"));
		existData.setProductPrice(productJson.getDouble("product_Price"));
		existData.setProductQuantity(productJson.getInt("product_Qnty"));
		existData.setStock(productJson.getInt("stock"));

		return productRep.save(existData);

	}

	// -----------------Delete the Product---------------------------//

	@CrossOrigin()
	@DeleteMapping("/deleteProductByID/{inputID}")
	public String deleteProductByID(@PathVariable Long inputID) throws Exception {

		productRep.deleteById(inputID);

		return "product Deleted successfully"; // Return the updated product list
	}

	// --getAllProduct-----------//

	@CrossOrigin()
	@GetMapping("/getAllProductDeatils")
	public List<ProductDetail> getAllProductDetails() throws Exception {

		return productRep.findAll();

	}

	// ---Add To Cart--------------//
	@CrossOrigin
	@PostMapping("/addToCart")
	public AddCart addToCart(@RequestBody Map<String, Object> userCartDetails) throws Exception {
System.out.println("---"+userCartDetails);
		String userid = (String) userCartDetails.get("userid");
//		String inputCartid = (String) userCartDetails.get("cartid");

		int product_ID = (int) userCartDetails.get("product_ID");
		long productIDLong = (long) product_ID;

		ProductDetail productData = productRep.findById(productIDLong).get();
		productData.setProductQuantity((int)userCartDetails.get("productQuantity"));
		UserMaster userDetail = userRep.findByUserName(userid);

		AddCart cartData = new AddCart();

		cartData.setProduct(productData);
		cartData.setUser(userDetail);

		return cardRep.save(cartData);
	}

	// ---- Get the Product From the Card

	@CrossOrigin
	@GetMapping("/getAddToCartDetail/{userName}")
	public List<AddCart> getAddToCartDetail(@PathVariable String userName) throws Exception {

		UserMaster userDetail = userRep.findByUserName(userName);

		Long userId = userDetail.getUserId();

		List<AddCart> productDetail = cardRep.findByUserUserId(userId);
List productList=new ArrayList<>();
		
		productDetail.forEach((e)->
		{
			productList.add(e.getProduct());
		});
		
		System.out.println("productDetail--" + productDetail);

		return productList;

	}
	
	//---Remove the Product From a card-----//
	
	@CrossOrigin
	@DeleteMapping("/removeFromCart")
	public String removeFromCart(@RequestBody Map<String, Object> input) throws Exception {
		
		int productIdStr = (int) input.get("product_ID");
		Long product_ID = (long)productIdStr;
		
		String userid = (String) input.get("userid");
		UserMaster userDetail = userRep.findByUserName(userid);
		Long UserId=userDetail.getUserId();
		
		cardRep.deleteByUserIdAndProductId(UserId, product_ID);
		
		
		return "Deleted successfully";

	}
	
	// ****************************** Start_WorkFlow_API's
	// ****************************

	@CrossOrigin
	@PostMapping(value = "/submitProductList")
	public Map<String, Object> startWorkflow(@RequestBody String reqBody) throws Exception {
		long generatedOrderId = orderIdCounter.getAndIncrement();
System.out.println("---"+reqBody);
		JSONObject jsonObj = new JSONObject(reqBody);

		String userid = (String) jsonObj.get("userId");
		UserMaster userDetail = userRep.findByUserName(userid);

		// Camunda-related logic ...

		Map<String, Object> inputVariable = new HashMap<>();

		// Authenticate with Camunda Task List
		SaasAuthentication sa = new SaasAuthentication("zCCx7DH6.mn_tGC5O1mNrPg-q6qVNsMg",
				"u4Y-WP8SmmVik3E.PJ.iE0-09wSX_rj5LLGSKVhL~Y23ZcXP_QXPgnPiAVCcezM_");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
				.shouldReturnVariables().authentication(sa).build();

		// Parse the request body
		

		ProcessInstanceEvent processInstEvnt = zeebeClient.newCreateInstanceCommand().bpmnProcessId("OrderFullfillment")
				.latestVersion().variables(inputVariable).send().join();

		long pID = processInstEvnt.getProcessInstanceKey();
		long processDefinitionKey = processInstEvnt.getProcessDefinitionKey();

		Map<String, Object> processIdMap = new HashMap<>();
	    processIdMap.put("processInstanceKey", pID);
		processIdMap.put("orderId", generatedOrderId);
		processIdMap.put("processInstanceKey", pID);
		processIdMap.put("paymentStatus", " ");
		processIdMap.put("productDetail", inputVariable);

		OrderDetail order = new OrderDetail();
		order.setOrderId(generatedOrderId);
		order.setOrderStatus("Processing");
		order.setPaymentStatus("Pending");
		order.setProcessInstanceKey(pID);
		order.setUser(userDetail);

		orderRep.save(order);

		return processIdMap;
	}
	
	
	// --- Get Customer order History-----//

	@GetMapping(value = "/getCustomerHistory/{userName}")
	public List<OrderDetail> getHistory(@PathVariable String userName) throws org.json.simple.parser.ParseException {
		
		UserMaster userDetail = userRep.findByUserName(userName);

		Long userId = userDetail.getUserId();
		
	List<OrderDetail> orderData=orderRep.findByUserUserId(userId);
		
		
		
		return orderData;

	}
	
	
	
	
	
	
	

}
