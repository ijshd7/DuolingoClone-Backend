package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pfp_source")
    private String pfpSrc;

    @Column(name="points")
    private String points;

    @Column(name="created_at")
    private Timestamp createdAt;

}
