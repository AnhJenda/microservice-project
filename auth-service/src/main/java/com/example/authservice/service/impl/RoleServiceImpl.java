package com.example.authservice.service.impl;

import com.example.authservice.entity.Role;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleRepository repository;

    @Override
    public List<Role> gets() {
        return repository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        Optional<Role> RoleOptional = repository.findById(id);

        if (RoleOptional.isPresent()) {
            return RoleOptional.get(); // Extract the Role object from Optional
        } else {
            return null; // Return null if Role is not found
        }
    }

    @Override
    public Role save(Role Role) {
        return  repository.save(Role);
    }



}
