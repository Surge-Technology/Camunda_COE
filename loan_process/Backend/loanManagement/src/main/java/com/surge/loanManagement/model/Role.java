package com.surge.loanManagement.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROLE_DETAILS")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_ID")
	private long roleId;

	@Column(name = "ROLE_NAME", unique = true, nullable = false)
	private String roleName;

	@OneToMany(mappedBy = "role")
	@JsonIgnore
	private Set<User> users;

	public long getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}