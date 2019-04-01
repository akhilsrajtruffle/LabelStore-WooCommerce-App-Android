
package com.mavsoft.label.Models.WpGetUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpGetUserData.WpGetUserData;

public class WpGetUser {

	@SerializedName("data")
	@Expose
	private WpGetUserData data;

	public WpGetUserData getData() {
		return data;
	}

	public void setData(WpGetUserData data) {
		this.data = data;
	}
}
