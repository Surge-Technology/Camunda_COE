package com.camundaSaaS.vms.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.camundaSaaS.vms.model.VendorRegistration;

@Configuration
public class config {
	
	

	//public class AppConfig {

	//

	    @Bean

	    public VendorRegistration vendorRegistration() {

	        return new VendorRegistration();

	    }

}
