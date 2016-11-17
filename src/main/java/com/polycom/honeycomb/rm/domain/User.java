package com.polycom.honeycomb.rm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Chen on 2016/10/26.
 */
@Document(collection = "users") public class User {
    @Id private String id;

    private String   userName;
    private String   firstName;
    private String   lastName;
    private String   password;
    private String   email;
    private String[] poolList;
    private String[] roleList;

    @DBRef
    private List<ResourcePool> belongingPools = new ArrayList<ResourcePool>();

    @DBRef private List<Role> roles = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<ResourcePool> getBelongingPools() {
        return belongingPools;
    }

    public void setBelongingPools(List<ResourcePool> belongToPools) {
        this.belongingPools = belongToPools;
    }

    public String[] getPoolList() {
        return poolList;
    }

    public void setPoolList(String[] poolList) {
        this.poolList = poolList;
    }

    public String[] getRoleList() {
        return roleList;
    }

    public void setRoleList(String[] roleList) {
        this.roleList = roleList;
    }
}
