package com.camunda.orderfullfillment.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class OrderUtil {



	@Autowired
	private ResourceLoader resourceLoader;



	public JSONArray readFileMock(String filePath) throws Exception {
	    System.out.println("readFileMock method invoked 1.10...");

	    // Load the resource from the classpath
	    ClassPathResource cpr = new ClassPathResource(filePath, this.getClass().getClassLoader());
	    
	    try (InputStream inputStream = cpr.getInputStream();
		         InputStreamReader reader = new InputStreamReader(inputStream)) {
		        
		        // Convert InputStream to String
		        String fileContent = IOUtils.toString(reader);
		        
		        // Parse the JSON content
		        JSONParser parser = new JSONParser();
		        Object obj = parser.parse(fileContent);
		        JSONArray jsonArray = (JSONArray) obj;

		        return jsonArray;
		    } catch (IOException e) {
		        System.err.println("Error reading the file: " + e.getMessage());
		        throw e; // Rethrow or handle as needed
		    }

	}
	
	
	 public Path getWritableFilePath(String fileName) {
		 
		 System.out.println("----"+Path.of("jsonreader", fileName));
		 
	        return Path.of("jsonreader", fileName); // Specify your desired output directory
	    }
	 
	 public void writeDataToJsonFile(String fileName, Map<String,Object> newData) {
	        ObjectMapper objectMapper = new ObjectMapper();
	        Path filePath = getWritableFilePath(fileName);
	        // Create the output directory if it doesn't exist
	        try {
	            Files.createDirectories(filePath.getParent()); // Ensure the output directory exists
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        try {
	            // Load existing data if any
	            ArrayNode existingData = objectMapper.createArrayNode();

	            // Check if the file already exists
	          
	            if (Files.exists(filePath)) {
	                existingData = (ArrayNode) objectMapper.readTree(Files.readString(filePath));
	            }
	            ObjectNode newData1 = objectMapper.convertValue(newData, ObjectNode.class);
	            // Add new data to the existing data
	            existingData.add(newData1); // Append new data

	            // Write the combined data back to the file
	            try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
	                objectMapper.writeValue(fileWriter, existingData); // Write updated data to the file
	                System.out.println("Data written to " + fileName);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	
	public Object readCustomerHistroy(String filePath) throws Exception {
	    System.out.println("readFileMock method invoked 1.10...");

	    // Load the resource from the classpath
	    ClassPathResource cpr = new ClassPathResource(filePath, this.getClass().getClassLoader());
	    System.out.println("cpr  path---"+cpr.getFile().toPath());
	    try (InputStream inputStream = cpr.getInputStream();
		         InputStreamReader reader = new InputStreamReader(inputStream)) {
		        
		        // Convert InputStream to String
		        String fileContent = IOUtils.toString(reader);
		        
		        // Parse the JSON content
		        JSONParser parser = new JSONParser();
		        Object obj = parser.parse(fileContent);
		        
		        return obj;
		    } catch (IOException e) {
		        System.err.println("Error reading the file: " + e.getMessage());
		        throw e; // Rethrow or handle as needed
		    }

	}

	public InputStream getFileInputStream(String fileName) throws Exception {
	    System.out.println("getFileInputStream method invoked...");
	    ClassPathResource cpr = new ClassPathResource(fileName, this.getClass().getClassLoader());

	    // Instead of getting the file path, get an InputStream
	    InputStream inputStream = cpr.getInputStream();
	    System.out.println("InputStream opened for file: " +fileName);
	    
	    return inputStream;
	}

	

	

		public Path getFilePath(String fileName) throws Exception {
			System.out.println("getFilePath method invoked 1.10...");
		    ClassPathResource cpr = new ClassPathResource(fileName, this.getClass().getClassLoader());
		    System.out.println(cpr.getFile().getPath());
		    Path path = cpr.getFile().toPath();
		    System.out.println("getFilePath----"+path);
		    
			return path;
		   
		}
		
		public Path getFilePath1(String fileName) throws Exception {
	        System.out.println("getFilePath method invoked...");
	        
	        // Use ClassPathResource to get the path
	        ClassPathResource cpr = new ClassPathResource(fileName, this.getClass().getClassLoader());
	        System.out.println("cpr--"+cpr.getFile().getPath());
	        System.out.println("getAbsolutePath--"+cpr.getFile().getAbsolutePath());
	        // Create a temporary file to work with, if necessary
	        Path tempFile = cpr.getFile().toPath();
	        
	        // Copy content to the temporary file (only if you need to write)
	        try (InputStream inputStream = cpr.getInputStream()) {
	            Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	        }
	        
	        System.out.println("getFilePath----" + tempFile);
	        return tempFile;
	    }
	
	 public Path getDynamicFilePath(String fileName) {
	        // Example 1: Store in user's home directory
	        String userHome = System.getProperty("user.home");
	        Path userHomeFilePath = Paths.get(userHome, "OrderFiles", fileName);

	        // Example 2: Store in application directory (relative path)
	        Path relativeFilePath = Paths.get("OrderFiles", fileName);

	        // Example 3: Store in environment-specified directory
	        String envDirectory = System.getenv("ORDER_STORAGE_PATH");
	        Path envFilePath = (envDirectory != null) ? Paths.get(envDirectory, fileName) : relativeFilePath;

	        // Example: Choose one of the options, based on preference
	        return envFilePath;  // Using environment-based path or fallback to relative
	    }
	 
	 
	 
	 
		public String readJsonFile(String fileName) {

			String path = OrderUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();

			System.out.println("path---" + path);

			String jsonFilePath;

			if (path.contains("!")) {
				// Running from JAR, get the working directory
				jsonFilePath = Paths.get("").toAbsolutePath().resolve(fileName).toString();
			} else {
				// Not running from JAR, use the standard method
				jsonFilePath = Paths.get("").toAbsolutePath().resolve(fileName).toString();
			}
			return jsonFilePath;

		}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 


}
