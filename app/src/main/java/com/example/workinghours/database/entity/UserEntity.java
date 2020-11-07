package com.example.workinghours.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "users", primaryKeys = {"email"})
public class UserEntity implements Comparable {

    @NonNull
    private String email;

    private String password;

    @Ignore
    public UserEntity() {
    }

    public UserEntity(@NonNull String email, String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

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
}
