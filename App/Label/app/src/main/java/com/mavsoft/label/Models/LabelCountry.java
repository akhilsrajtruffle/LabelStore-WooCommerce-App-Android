package com.mavsoft.label.Models;

public class LabelCountry {
    public String iso;

    public String code;

    public String name;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return iso + " - " + code + " - " + name.toUpperCase();
    }
}
