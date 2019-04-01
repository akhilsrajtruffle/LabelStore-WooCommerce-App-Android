package com.mavsoft.label.Models;

import com.google.dexmaker.Label;

import java.io.Serializable;

/**
 * Created by agmm on 12/09/2017.
 */

public class LabelAddress implements Serializable {

    public String street;
    public String city;
    public String county;
    public String postcode;
    public String country;

    public LabelAddress() {
        street = "";
        city = "";
        county = "";
        postcode = "";
        country = "";
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String createAddress() {
        String str = "";
        
        str = ((street.equals("") ? "" : str + street + ", "));
        str = ((city.equals("") ? "" : str + city + ", "));
        str = ((county.equals("") ? "" : str + county + ", "));
        str = ((postcode.equals("") ? "" : str + postcode + ", "));
        str = ((country.equals("") ? "" : str + country));

        return str;
    }
}
