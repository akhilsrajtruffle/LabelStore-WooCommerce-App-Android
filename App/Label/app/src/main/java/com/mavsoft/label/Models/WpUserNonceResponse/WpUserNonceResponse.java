
package com.mavsoft.label.Models.WpUserNonceResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpUserNonceResponseResult.WpUserNonceResponseResult;

public class WpUserNonceResponse {

	@SerializedName("result")
	@Expose
	private WpUserNonceResponseResult result;

	public WpUserNonceResponseResult getResult() {
		return result;
	}

	public void setResult(WpUserNonceResponseResult result) {
		this.result = result;
	}
}
