package com.cloud.ocs.portal.common.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user", catalog = "cloud_ocs_portal")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private Integer id;
	private String accountId;
	private String accountPassword;
	private String name;
	private String email;
	private String phoneNumber;
	private Timestamp lastLoginDate;

	// Constructors
	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String accountId, String accountPassword, String name) {
		this.accountId = accountId;
		this.accountPassword = accountPassword;
		this.name = name;
	}

	/** full constructor */
	public User(String accountId, String accountPassword, String name,
			String email, String phoneNumber, Timestamp lastLoginDate) {
		this.accountId = accountId;
		this.accountPassword = accountPassword;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.lastLoginDate = lastLoginDate;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "account_id", nullable = false, length = 20)
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Column(name = "account_password", nullable = false, length = 30)
	public String getAccountPassword() {
		return this.accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone_number", length = 20)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "last_login_date", length = 19)
	public Timestamp getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

}