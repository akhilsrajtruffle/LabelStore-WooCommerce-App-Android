
package com.mavsoft.label.Models.WpRegisterPostData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpRegisterPostData {

	@SerializedName("username")
	@Expose
	private String username;

	@SerializedName("email")
	@Expose
	private String email;

	@SerializedName("nonce")
	@Expose
	private String nonce;

	@SerializedName("first_name")
	@Expose
	private String first_name;

	@SerializedName("last_name")
	@Expose
	private String last_name;

	@SerializedName("password")
	@Expose
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
