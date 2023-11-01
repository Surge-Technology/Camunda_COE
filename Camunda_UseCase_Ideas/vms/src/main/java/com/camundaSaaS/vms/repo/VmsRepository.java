package com.camundaSaaS.vms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camundaSaaS.vms.model.VendorRegistration;

@Repository
public interface VmsRepository extends JpaRepository<VendorRegistration, Integer> {

	
	
}
