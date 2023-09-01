package com.camundaSaaS.vms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camundaSaaS.vms.model.VendorRegistration;
import com.camundaSaaS.vms.repo.VmsRepository;

@Service
public class VmsService {
	
	@Autowired
    private  VmsRepository vmsRepo ;
//	
//	@Autowired
//	priva
//
//    @Autowired
//    public void PersonService(VmsRepository vmsRepo) {
//        this.vmsRepo = personRepository;
//    }

	
	
	@Autowired

	private VmsRepository vmsRepository;

	public VendorRegistration saveVendor(VendorRegistration vpApplicationModel) {

		return vmsRepository.save(vpApplicationModel);

	}
	
	
	
   
}
