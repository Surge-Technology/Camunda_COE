package com.camunda.orderfullfillment.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserMaster") // Specify table name if needed
public class UserMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId; // Changed to lowercase 'u'
	
	@Column(name = "UserName") // Explicitly specify column name
	private String userName; // Changed to lowercase 'u'
	
	@Column(name = "EmailId") // Explicitly specify column name
	private String emailId; // Changed to lowercase 'e'
	
	@Column(name = "UserPassWord") // Explicitly specify column name
	private String userPassWord; // Changed to lowercase 'u' and 'p'

	// Getters and Setters
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserPassWord() {
		return userPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}
}
