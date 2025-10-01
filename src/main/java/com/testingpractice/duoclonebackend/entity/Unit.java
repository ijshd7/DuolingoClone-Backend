package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "units")
@Data
@NoArgsConstructor
public class Unit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "animation_path")
  private String animationPath;

  @Column(name = "color")
  private String color;

  @Column(name = "description")
  private String description;

  @Column(name = "order_index")
  private Integer orderIndex;

  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "section_id")
  private Integer sectionId;

  @Builder
  public Unit(
      String title,
      String description,
      Integer orderIndex,
      Integer courseId,
      Integer sectionId,
      String animationPath,
      String color) {
    this.title = title;
    this.description = description;
    this.orderIndex = orderIndex;
    this.courseId = courseId;
    this.sectionId = sectionId;
    this.color = color;
    this.animationPath = animationPath;
  }
}
