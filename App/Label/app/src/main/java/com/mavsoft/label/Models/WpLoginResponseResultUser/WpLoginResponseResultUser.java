
package com.mavsoft.label.Models.WpLoginResponseResultUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpLoginResponseResultUser {

	
	@SerializedName("firstname")
	@Expose
	private String firstname;
	
	@SerializedName("lastname")
	@Expose
	private String lastname;

	@SerializedName("email")
	@Expose
	private String email;

	@SerializedName("id")
	@Expose
	private Integer id;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
