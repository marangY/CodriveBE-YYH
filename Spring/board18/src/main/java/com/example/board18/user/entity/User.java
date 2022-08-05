package com.example.board18.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    public String userID;

    public String name;

    public String password;

    public String email;
}
