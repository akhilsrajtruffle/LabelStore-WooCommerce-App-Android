
package com.mavsoft.label.Models.WpGetUserData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpGetUserData {

	@SerializedName("user_id")
	@Expose
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
