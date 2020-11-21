package com.example.workinghours.database.entity;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class UserEntity implements Comparable {

    private String id;
    private String email;
    private String password;

    public UserEntity() {
    }

    public UserEntity(@NonNull String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Exclude
    public String getId(){ return id;}

    public void setId(String id){this.id=id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserEntity)) return false;
        UserEntity o = (UserEntity) obj;
        return o.getEmail().equals(this.getEmail());
    }

    @Override
    public String toString() { return email; }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);

        return result;
    }
}
