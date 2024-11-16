package com.camunda.orderfullfillment.Rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camunda.orderfullfillment.Entity.UserMaster;

@Repository
public interface UserMasterRep extends JpaRepository<UserMaster,Long>{

	
	UserMaster findByUserName(String UserName);
}
