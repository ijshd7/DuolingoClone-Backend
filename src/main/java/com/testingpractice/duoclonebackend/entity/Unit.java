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

    @Column(name = "description")
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "section")
    private Integer section;

    @Builder
    public Unit(String title,
                String description,
                Integer orderIndex,
                Integer courseId,
                Integer section) {
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.courseId = courseId;
        this.section = section;
    }
}
