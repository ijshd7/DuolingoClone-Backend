package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lesson_completions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonCompletion {

    @EmbeddedId
    private LessonCompletionId id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "completed_at")
    private Timestamp completedAt;

    public Integer getUserId() { return id != null ? id.getUserId() : null; }
    public void setUserId(Integer userId) {
        if (id == null) id = new LessonCompletionId();
        id.setUserId(userId);
    }
    public Integer getLessonId() { return id != null ? id.getLessonId() : null; }
    public void setLessonId(Integer lessonId) {
        if (id == null) id = new LessonCompletionId();
        id.setLessonId(lessonId);
    }

}
