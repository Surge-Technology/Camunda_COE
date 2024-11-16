package com.camunda.orderfullfillment.Entity;

import javax.persistence.*;

@Entity
@Table(name = "AddCart")
public class AddCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate cart_id
    @Column(name = "cart_id")
    private Long cart_id;

    @ManyToOne // Use ManyToOne if one cart can contain multiple products
    @JoinColumn(name = "productId", referencedColumnName = "productId") // Foreign key to ProductDetail
    private ProductDetail product; // Change this to hold a single product

    @ManyToOne	
    @JoinColumn(name = "userId", referencedColumnName = "userId") // Foreign key to UserMaster
    private UserMaster user;

    // Getters and Setters
    public Long getCart_id() {
        return cart_id;
    }

    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AddCart [cart_id=" + cart_id + ", product=" + product + ", user=" + user + "]";
    }
}
