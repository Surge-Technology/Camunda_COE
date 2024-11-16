package com.camunda.orderfullfillment.Rep;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.camunda.orderfullfillment.Entity.AddCart;

public interface CardRep extends JpaRepository<AddCart, Long>{

	List<AddCart> findByUserUserId(Long userId);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM AddCart c WHERE c.user.userId = :userId AND c.product.productId = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
	
}
