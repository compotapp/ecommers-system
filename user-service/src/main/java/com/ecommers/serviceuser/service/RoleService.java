package com.ecommers.serviceuser.service;

import com.ecommers.serviceuser.config.PlainAuthentication;
import com.ecommers.serviceuser.entity.UserRole;
import com.ecommers.serviceuser.repository.RoleRepository;
import com.ecommers.serviceuser.role.Role;
import com.ecommers.serviceuser.role.RoleType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service("RoleService")
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public boolean hasAnyRole(Role... roles) {
        final Long userId = ((PlainAuthentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        final Set<RoleType> roleTypes =
                roleRepository.findRoleTypesByUserId(userId);
        for (Role role : roles) {
            if (roleTypes.stream().anyMatch(roleType -> roleType.includes(role))) {
                return true;
            }
        }
        return false;
    }
}
