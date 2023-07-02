package com.backend.todolist.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @NotEmpty(message = "Username is required")
    @Column(unique = true)
    private String username;
    
    @NotEmpty(message = "Password is required")
    private String pass;

    private String userRole;
    
    protected User() {
		
	}

    public User(String username, String password) {
		super();
		this.username = username;
		this.pass = password;
		this.userRole = "User";
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRoleAsList() {
        return Collections.singletonList(this.userRole);
    }

    public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String role) {
        this.userRole = role;
    }
}