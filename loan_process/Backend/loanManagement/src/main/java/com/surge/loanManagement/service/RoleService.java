package com.surge.loanManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.loanManagement.model.Role;
import com.surge.loanManagement.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	   public Role createRole(Role role) {
	        return roleRepository.save(role);
	    }

	    public List<Role> getAllRoles() {
	        return roleRepository.findAll();
	    }

	    public Role getRoleById(long roleId) {
	        return roleRepository.findById(roleId)
	                .orElseThrow(() -> new RuntimeException("Role not found"));
	    }

	    public Role updateRole(long roleId, Role role) {
	        Role existingRole = getRoleById(roleId);
	        existingRole.setRoleName(role.getRoleName());
	        return roleRepository.save(existingRole);
	    }

	    public void deleteRole(long roleId) {
	        roleRepository.deleteById(roleId);
	    }
}
