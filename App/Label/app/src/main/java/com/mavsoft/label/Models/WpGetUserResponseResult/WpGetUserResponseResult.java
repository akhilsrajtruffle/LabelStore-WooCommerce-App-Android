
package com.mavsoft.label.Models.WpGetUserResponseResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpGetUserResponseResult {

	@SerializedName("first_name")
	@Expose
	private String first_name;

	@SerializedName("last_name")
	@Expose
	private String last_name;

	@SerializedName("email")
	@Expose
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
