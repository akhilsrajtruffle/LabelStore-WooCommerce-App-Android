
package com.mavsoft.label.Models.WpGetUserPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpGetUserPostData.WpGetUserPostData;
import com.mavsoft.label.Models.WpRegisterPostData.WpRegisterPostData;

public class WpGetUserPost {

	@SerializedName("data")
	@Expose
	private WpGetUserPostData data;

	public WpGetUserPostData getData() {
		return data;
	}

	public void setData(WpGetUserPostData data) {
		this.data = data;
	}
}
