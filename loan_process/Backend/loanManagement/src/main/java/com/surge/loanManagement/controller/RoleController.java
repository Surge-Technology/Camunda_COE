package com.surge.loanManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.surge.loanManagement.model.Role;
import com.surge.loanManagement.service.RoleService;

@RestController
public class RoleController {

	@Autowired
	RoleService roleService;
	
    @PostMapping("/createRole")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    @GetMapping("/getAllRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/getRoleById/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable long roleId) {
        Role role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/updateRole/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable long roleId, @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(roleId, role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/deleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
