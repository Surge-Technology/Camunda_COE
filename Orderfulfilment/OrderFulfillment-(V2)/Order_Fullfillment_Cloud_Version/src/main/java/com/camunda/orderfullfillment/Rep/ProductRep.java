package com.camunda.orderfullfillment.Rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camunda.orderfullfillment.Entity.ProductDetail;

@Repository
public interface ProductRep extends JpaRepository<ProductDetail, Long>{

}
