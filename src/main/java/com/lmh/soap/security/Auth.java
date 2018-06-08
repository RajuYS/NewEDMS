package com.lmh.soap.security;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Auth {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="USER_ID")
	private Integer user_id;

	@Column(name="ENTRY_DATE",nullable = false)
	private Timestamp entry_date;

	@Column(name="FULL_NAME",nullable = false)
	private String full_name;

	@Column(name="USER_NAME",nullable = false, unique = true)
	private String user_name;

	@Column(name="SECRET",nullable = false)
	private String secret;

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setEntry_date(Timestamp entry_date) {
		this.entry_date = entry_date;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public Timestamp getEntry_date() {
		return entry_date;
	}

	public String getFull_name() {
		return full_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getSecret() {
		return secret;
	}

}
