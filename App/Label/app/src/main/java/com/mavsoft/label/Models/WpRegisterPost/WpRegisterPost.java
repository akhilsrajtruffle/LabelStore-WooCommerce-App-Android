
package com.mavsoft.label.Models.WpRegisterPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpRegisterPostData.WpRegisterPostData;

public class WpRegisterPost {

	@SerializedName("data")
	@Expose
	private WpRegisterPostData data;

	public WpRegisterPostData getData() {
		return data;
	}

	public void setData(WpRegisterPostData data) {
		this.data = data;
	}
}
