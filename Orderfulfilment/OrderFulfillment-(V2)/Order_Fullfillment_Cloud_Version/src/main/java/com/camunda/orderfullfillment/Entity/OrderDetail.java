package com.camunda.orderfullfillment.Entity;

import java.util.List;

import javax.persistence.*;

import com.apollographql.apollo3.api.BooleanExpression.True;

@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(name = "ProcessInstanceKey", nullable = false)
	private Long processInstanceKey;

	@Column(name = "OrderStatus", nullable = false)
	private String orderStatus;

	@Column(name = "PaymentStatus", nullable = false)
	private String paymentStatus;

	@ManyToMany
	@JoinTable(
	    name = "OrderDetail",
	    joinColumns = @JoinColumn(name = "orderId"),
	    inverseJoinColumns = @JoinColumn(name = "productId")
	)
	private List<ProductDetail> products; // This will be allowed to be null

	
	
	 @ManyToOne
	    @JoinColumn(name = "userId", referencedColumnName = "userId")  // Foreign key to UserMaster
	    private UserMaster user;


	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	public Long getProcessInstanceKey() {
		return processInstanceKey;
	}


	public void setProcessInstanceKey(Long processInstanceKey) {
		this.processInstanceKey = processInstanceKey;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public List<ProductDetail> getProducts() {
		return products;
	}


	public void setProducts(List<ProductDetail> products) {
		this.products = products;
	}


	public UserMaster getUser() {
		return user;
	}


	public void setUser(UserMaster user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "OrderDetail [orderId=" + orderId + ", processInstanceKey=" + processInstanceKey + ", orderStatus="
				+ orderStatus + ", paymentStatus=" + paymentStatus + ", products=" + products + ", user=" + user + "]";
	}



	
	 

	
	

	
	
}
