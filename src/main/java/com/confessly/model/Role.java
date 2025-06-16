package com.confessly.model;

public class Role {
     public static final String ADMIN_ROLE = "admin";
    public static final String USER_ROLE = "user";

    private String roleName;

    public Role(String roleName) {
        if (!validRole(roleName)) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        if (!validRole(roleName)) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
        this.roleName = roleName;
    }

    public static String getRoleAdmin() {
        return ADMIN_ROLE;
    }

    public static String getRoleUser() {
        return USER_ROLE;
    }

    public static boolean validRole(String role) {
        return role != null && (role.equalsIgnoreCase(ADMIN_ROLE) || role.equalsIgnoreCase(USER_ROLE));
    }

    @Override
    public String toString() {
        return roleName;
    }
} 