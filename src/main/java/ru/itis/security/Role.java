package ru.itis.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum Role {
    USER("USER"), ADMIN("ADMIN"), SUPERADMIN("SUPERADMIN");

    private String val;

    Role(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
