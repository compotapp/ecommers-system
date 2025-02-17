package com.ecommers.serviceuser.repository;

import com.ecommers.serviceuser.entity.UserRole;
import com.ecommers.serviceuser.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
        SELECT ur.type FROM UserRole ur
        WHERE ur.user.id = :userId
        """)
    Set<RoleType> findRoleTypesByUserId(Long userId);
}
