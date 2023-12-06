package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.Role;
import com.unipi.pfatouros.eAssist_backend.entity.RoleEnum;
import com.unipi.pfatouros.eAssist_backend.entity.User;
import com.unipi.pfatouros.eAssist_backend.repository.RoleRepository;
import com.unipi.pfatouros.eAssist_backend.repository.UserRepository;
import com.unipi.pfatouros.eAssist_backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    // Implementation of methods which contain the business logic related to the Role entity

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Set<Role> getRolesFromRequest(Set<String> strRoles) {

        // Convert a set of Strings into a set of RoleModel objects
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin" -> {
                    Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                case "manager" -> {
                    Role managerRole = roleRepository.findByName(RoleEnum.ROLE_MANAGER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(managerRole);
                }
                case "employee" -> {
                    Role employeeRole = roleRepository.findByName(RoleEnum.ROLE_EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(employeeRole);
                }
                default -> throw new RuntimeException("Error: Role not found.");
            }
        });

        return roles;
    }

    @Override
    public List<Role> getRoles() {

        // Select all roles from the database
        return roleRepository.findAll();
    }

    @Override
    public void addRole(String name) {

        // Check if another role with the same name already exists in the database
        boolean existsByName = roleRepository.existsByName(RoleEnum.valueOf(name));
        if (existsByName) {
            String message = "Error: Role with name " + name + " already exists!";
            throw new IllegalStateException(message);
        }

        // Create new role
        Role role = new Role(RoleEnum.valueOf(name));

        // Insert role into the database
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {

        // True if role exists, False otherwise
        boolean exists = roleRepository.existsById(id);

        // If role does not exist, throw exception
        if (!exists) {
            String message = "Role with id " + id + " does not exist!";
            throw new IllegalStateException(message);
        }

        // Delete user from the database
        roleRepository.deleteById(id);
    }

    @Override
    public Boolean hasRoleEmployee(String username) {

        // Get user
        User user = userRepository.findById(username)
                .orElseThrow(() -> {
                    String message = "User with username " + username + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Get role
        Role role = roleRepository.findByName(RoleEnum.ROLE_EMPLOYEE)
                .orElseThrow(() -> {
                    String message = "Role with name " + RoleEnum.ROLE_EMPLOYEE.name() + " does not exist!";
                    return new IllegalStateException(message);
                });

        return user.getRoles().contains(role);
    }

    @Override
    public Boolean hasRoleManager(String username) {

        // Get user
        User user = userRepository.findById(username)
                .orElseThrow(() -> {
                    String message = "User with username " + username + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Get role
        Role role = roleRepository.findByName(RoleEnum.ROLE_MANAGER)
                .orElseThrow(() -> {
                    String message = "Role with name " + RoleEnum.ROLE_MANAGER.name() + " does not exist!";
                    return new IllegalStateException(message);
                });

        return user.getRoles().contains(role);
    }

    @Override
    public Boolean hasRoleAdmin(String username) {

        // Get user
        User user = userRepository.findById(username)
                .orElseThrow(() -> {
                    String message = "User with username " + username + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Get role
        Role role = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                .orElseThrow(() -> {
                    String message = "Role with name " + RoleEnum.ROLE_ADMIN.name() + " does not exist!";
                    return new IllegalStateException(message);
                });

        return user.getRoles().contains(role);
    }
}
