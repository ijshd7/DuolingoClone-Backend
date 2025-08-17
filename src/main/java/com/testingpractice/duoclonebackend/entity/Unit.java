package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "course_id")
    private Integer courseIndex;

}
