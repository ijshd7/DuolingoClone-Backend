package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Lesson {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;

   @Column(name = "title")
    private String title;

   @Column(name = "unit_id")
    private Integer unitId;

   @Column(name = "order_index")
    private Integer orderIndex;

}
