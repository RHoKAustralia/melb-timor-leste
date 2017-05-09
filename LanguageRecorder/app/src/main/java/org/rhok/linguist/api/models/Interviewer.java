package org.rhok.linguist.api.models;

import java.io.Serializable;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class Interviewer implements Serializable{
    private int id;

    private String name;
    private String mobile;
    private String email;
    private String device_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
