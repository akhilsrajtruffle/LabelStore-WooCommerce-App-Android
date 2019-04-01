
package com.mavsoft.label.Models.WpGetUserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpGetUserResponseResult.WpGetUserResponseResult;

public class WpGetUserResponse {

	@SerializedName("status")
	@Expose
	private String status;

	@SerializedName("result")
	@Expose
	private WpGetUserResponseResult result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WpGetUserResponseResult getResult() {
		return result;
	}

	public void setResult(WpGetUserResponseResult result) {
		this.result = result;
	}
}
