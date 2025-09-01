package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "prompt")
    private String title;

    @Column(name = "lesson_id")
    private Integer lessonId;

    @Column(name = "type")
    private String type;

    @Column(name = "order_index")
    private Integer orderIndex;


}
