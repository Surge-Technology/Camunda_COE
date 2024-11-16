package com.camunda.orderfullfillment.Rep;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.camunda.orderfullfillment.Entity.OrderDetail;

public interface OrderRep extends JpaRepository<OrderDetail, Long>{

	 List<OrderDetail> findByUserUserId(Long userId);
	
}
