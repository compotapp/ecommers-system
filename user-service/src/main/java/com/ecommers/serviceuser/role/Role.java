package com.ecommers.serviceuser.role;

import java.util.Set;

public interface Role {
    boolean includes(Role role);

    static Set<Role> roots() {
        return Set.of(RoleType.ADMIN);
    }
}