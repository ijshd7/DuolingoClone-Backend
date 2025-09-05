package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "image_src")
  private String imgSrc;
}
