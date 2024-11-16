package com.camunda.orderfullfillment.Entity;


import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "ProductDetail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private double productPrice;
    private int productQuantity;
    private int stock;

    @ManyToMany(mappedBy = "products") 
    private List<OrderDetail> orders;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<OrderDetail> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetail> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "ProductDetail [productId=" + productId + ", productName=" + productName + ", productPrice="
                + productPrice + ", productQuantity=" + productQuantity + ", stock=" + stock + ", orders=" + orders + "]";
    }
}

