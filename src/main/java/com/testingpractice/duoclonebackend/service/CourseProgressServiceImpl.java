package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService{

    private final UserCourseProgressRepository userCourseProgressRepository;
    private final CurriculumNavigator curriculumNavigator;
    private final LessonRepository lessonRepository;
    private final LookupService lookupService;

    @Override
    @Transactional
    public UserCourseProgress updateUsersNextLesson (Integer userId, Integer courseId, Lesson currentLesson) {
       UserCourseProgress userCourseProgress =
               userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

       if (userCourseProgress == null)
           throw new ApiException(ErrorCode.USER_NOT_FOUND);


        if (userCourseProgress.getCurrentLessonId().equals(currentLesson.getId())) {

            // UPDATE USERS CURRENT LESSON
            Lesson nextLesson = curriculumNavigator.getNextLesson(currentLesson, userId, courseId);
            if (nextLesson == null)
                throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
            userCourseProgress.setCurrentLessonId(nextLesson.getId());
        }

        userCourseProgressRepository.save(userCourseProgress);

        return userCourseProgress;

   }

   @Override
   public Integer getLessonSectionId (Integer lessonId) {
        Lesson lesson = lookupService.lessonOrThrow(lessonId);
        Integer unitId = lesson.getUnitId();
       Unit unit = lookupService.unitOrThrow(unitId);
       Integer sectionId = unit.getSectionId();
       return sectionId;
   }

}
