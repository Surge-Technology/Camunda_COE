package com.camunda.orderfullfillment.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.rpc.context.AttributeContext.Resource;


@Component
public class fileUtil {
	
	 public Path getData(String fileName) throws IOException, URISyntaxException {
		 URL resource = getClass().getClassLoader().getResource("jsonreader/" + fileName);
	        
	        if (resource == null) {
	            throw new IOException("File not found in classpath: " + fileName);
	        }

	        // Convert the resource URL to a Path object
	        return Paths.get(resource.toURI());

	 }
}
