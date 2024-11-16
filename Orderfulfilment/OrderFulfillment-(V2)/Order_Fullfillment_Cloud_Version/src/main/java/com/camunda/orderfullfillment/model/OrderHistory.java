package com.camunda.orderfullfillment.model;

import java.util.Date;
import java.util.List;

public class OrderHistory {

	private Long orderId;
	
	private long processInstanceKey;

	private String orderStatus;

	private Date orderCreatedByDate;

	private List<ProductDetailss> products;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public long getProcessInstanceKey() {
		return processInstanceKey;
	}

	public void setProcessInstanceKey(long processInstanceKey) {
		this.processInstanceKey = processInstanceKey;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderCreatedByDate() {
		return orderCreatedByDate;
	}

	public void setOrderCreatedByDate(Date orderCreatedByDate) {
		this.orderCreatedByDate = orderCreatedByDate;
	}

	public List<ProductDetailss> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDetailss> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "OrderHistory [orderId=" + orderId + ", orderStatus=" + orderStatus + ", orderCreatedByDate="
				+ orderCreatedByDate + ", products=" + products + "]";
	}

	
	
	
}
