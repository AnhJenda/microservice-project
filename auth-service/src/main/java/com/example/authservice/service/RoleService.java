package com.example.authservice.service;

import com.example.authservice.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> gets();
    Role getRoleById(Long id);
    Role save(Role role);
}
