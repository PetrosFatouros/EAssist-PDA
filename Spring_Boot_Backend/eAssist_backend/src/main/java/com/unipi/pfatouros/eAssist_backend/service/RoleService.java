package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Validated
public interface RoleService {

    // Declaration of methods which contain the business logic related to the Role entity

    // Get the roles of a user (convert String to Role)
    Set<Role> getRolesFromRequest(@NotEmpty Set<String> strRoles);

    // Get a list that contains all existing roles
    List<Role> getRoles();

    // Create a new role
    void addRole(@NotBlank String name);

    // Delete a existing role
    void deleteRole(@NotNull Long id);

    // Return True if user has employee role, False otherwise
    Boolean hasRoleEmployee(@NotBlank String username);

    // Return True if user has manager role, False otherwise
    Boolean hasRoleManager(@NotBlank String username);

    // Return True if user has admin role, False otherwise
    Boolean hasRoleAdmin(@NotBlank String username);
}
