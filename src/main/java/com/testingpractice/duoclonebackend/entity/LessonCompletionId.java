package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LessonCompletionId implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "lesson_id")
    private Integer lessonId;

    public LessonCompletionId() {}
    public LessonCompletionId(Integer userId, Integer lessonId) {
        this.userId = userId; this.lessonId = lessonId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonCompletionId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lessonId);
    }
}