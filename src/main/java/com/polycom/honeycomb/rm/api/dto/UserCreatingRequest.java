package com.polycom.honeycomb.rm.api.dto;

/**
 * Created by Tao Chen on 2016/11/8.
 */
public class UserCreatingRequest {
    private String   userName;
    private String   firstName;
    private String   lastName;
    private String   email;
    private String   password;
    private String[] belongToPools;
    private String[] roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getBelongToPools() {
        return belongToPools;
    }

    public void setBelongToPools(String[] belongToPools) {
        this.belongToPools = belongToPools;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
