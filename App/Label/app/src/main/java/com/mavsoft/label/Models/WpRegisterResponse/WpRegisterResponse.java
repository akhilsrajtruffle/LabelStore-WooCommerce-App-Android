
package com.mavsoft.label.Models.WpRegisterResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpRegisterResponseResult.WpRegisterResponseResult;

public class WpRegisterResponse {

	@SerializedName("status")
	@Expose
	private String status;

	@SerializedName("result")
	@Expose
	private WpRegisterResponseResult result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WpRegisterResponseResult getResult() {
		return result;
	}

	public void setResult(WpRegisterResponseResult result) {
		this.result = result;
	}
}
