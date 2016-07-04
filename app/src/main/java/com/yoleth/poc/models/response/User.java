package com.yoleth.poc.models.response;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mael on 04/07/16.
 */
public class User extends RealmObject{

    @PrimaryKey
    private Long id;

    private String email;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
