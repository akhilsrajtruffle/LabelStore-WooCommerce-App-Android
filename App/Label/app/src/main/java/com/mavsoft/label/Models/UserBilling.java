package com.mavsoft.label.Models;

import java.io.Serializable;

/**
 * Created by agmm on 12/09/2017.
 */

public class UserBilling implements Serializable {

    String firstName;
    String lastName;
    String phone;
    String emailAddress;
    LabelAddress address;

    public UserBilling() {
        firstName = "";
        lastName = "";
        phone = "";
        emailAddress = "";

        address = new LabelAddress();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LabelAddress getAddress() {
        return address;
    }

    public void setAddress(LabelAddress address) {
        this.address = address;
    }

}
