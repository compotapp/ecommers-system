package com.ecommers.serviceuser.role;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum RoleType implements Role{
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String code;

    private final Set<Role> children = new HashSet<>();

    static {
        ADMIN.children.add(USER);
    }

    RoleType(String code) {
        this.code = code;
    }

    public static RoleType of(String code) {
        return Arrays.stream(values())
                .filter(roleType -> roleType.getCode().equals(code))
                .findFirst().orElse(null);
    }

    public static List<Role> of(List<String> codes) {
        return codes.stream()
                .map(RoleType::of)
                .collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean includes(Role role) {
        return this.equals(role) || children.stream().anyMatch(r -> r.includes(role));
    }

    @Component("Role")
    static class SpringComponent {
        private final RoleType ADMIN = RoleType.ADMIN;
        private final RoleType USER = RoleType.USER;

        public RoleType getADMIN() {
            return ADMIN;
        }

        public RoleType getUSER() {
            return USER;
        }
    }
}
