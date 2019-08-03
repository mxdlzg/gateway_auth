package com.pccc.team.auth.auth_service.entity;

public class LoginResult {
    private String type,currentAuthority;
    private String token;

    public LoginResult(String type, String currentAuthority, String token) {
        this.type = type;
        this.currentAuthority = currentAuthority;
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentAuthority() {
        return currentAuthority;
    }

    public void setCurrentAuthority(String currentAuthority) {
        this.currentAuthority = currentAuthority;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
