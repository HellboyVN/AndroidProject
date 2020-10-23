package com.workout.workout.modal;

public class User {
    String display_name;
    String email;
    String identifier;
    String mobile_number;
    String photo_url;
    String provider_id;
    String token;
    String user_uid;

    public User(String display_name, String email, String identifier, String mobile_number, String photo_url, String provider_id, String token, String user_uid) {
        this.display_name = display_name;
        this.email = email;
        this.identifier = identifier;
        this.mobile_number = mobile_number;
        this.photo_url = photo_url;
        this.provider_id = provider_id;
        this.token = token;
        this.user_uid = user_uid;
    }

    public String getDisplay_name() {
        return this.display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMobile_number() {
        return this.mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPhoto_url() {
        return this.photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getProvider_id() {
        return this.provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_uid() {
        return this.user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
