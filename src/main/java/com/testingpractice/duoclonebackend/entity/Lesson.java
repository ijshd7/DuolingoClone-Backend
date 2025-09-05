package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;

   @Column(name = "title")
    private String title;

   @Column(name = "unit_id")
    private Integer unitId;

   @Column(name = "lesson_type")
   private String lessonType;

   @Column(name = "order_index")
    private Integer orderIndex;


}
