package com.model;

import java.util.Date;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = { "username","email" }))
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private Long id;

	@NotNull
	@Size(min = 4, max = 30)
	private String username;

	@NotNull
	@Size(min = 4, max = 100)
	private String password;
	
	@NotNull
	@Size(min = 3)
	private String firstName;
	
	@NotNull
	@Size(min = 3)
	private String lastName;
	
	@NotNull
	private String email; 
	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<UserAuthority> authorities;
	
	@OneToMany(cascade = CascadeType.ALL , mappedBy="user")
	private Set<UserPhone> userPhones;
	
	@OneToMany(cascade = CascadeType.ALL , mappedBy="user")
	private Set<UserLaptop> userLaptop;

	public User() {
	}
	
	public User(String username) {
		this.username = username;
	
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(Long id, String password, String firstName, String lastName, Date birthDay) {
		this.id = id;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public User(String username, String password, String firstName, String lastName,String email) {
		super();

		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email=email;
	}
	
	public User(Long id, String username, String password, String firstName, String lastName,String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email=email;
	}
	
	
	
	public Set<UserLaptop> getUserLaptop() {
		return userLaptop;
	}

	public void setUserLaptop(Set<UserLaptop> userLaptop) {
		this.userLaptop = userLaptop;
	}

	public Set<UserPhone> getUserPhones() {
		return userPhones;
	}

	public void setUserPhones(Set<UserPhone> userPhones) {
		this.userPhones = userPhones;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void setAuthorities(Set<UserAuthority> authorities) {
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@JsonIgnore
	public Set<UserAuthority> getAuthorities() {
		return authorities;
	}

	// Use Roles as external API
	public Set<UserRole> getRoles() {
		Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
		if (authorities != null) {
			for (UserAuthority authority : authorities) {
				roles.add(UserRole.valueOf(authority));
			}
		}
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		for (UserRole role : roles) {
			grantRole(role);
		}
	}

	public void grantRole(UserRole role) {
		if (authorities == null) {
			authorities = new HashSet<UserAuthority>();
		}
		authorities.add(role.asAuthorityFor(this));
	}

	public void revokeRole(UserRole role) {
		if (authorities != null) {
			authorities.remove(role.asAuthorityFor(this));
		}
	}

	public boolean hasRole(UserRole role) {
		return authorities.contains(role.asAuthorityFor(this));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getUsername();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}