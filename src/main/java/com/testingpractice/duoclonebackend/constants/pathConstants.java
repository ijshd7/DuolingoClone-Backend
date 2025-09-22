package com.testingpractice.duoclonebackend.constants;

public class pathConstants {

  public static final String API_V1 = "/api";

  // ----------------------------- API PREFIXES -----------------------------
  public static final String COURSES = API_V1 + "/courses";
  public static final String EXERCISES = API_V1 + "/exercises";
  public static final String UNITS = API_V1 + "/units";
  public static final String USERS = API_V1 + "/users";
  public static final String LESSONS = API_V1 + "/lessons";
  public static final String SECTIONS = API_V1 + "/sections";

  // ----------------------------- GET REQUESTS -----------------------------
  public static final String GET_USER_COURSE_PROGRESS = "/progress/{courseId}/{userId}";
  public static final String GET_USER_BY_ID = "/{userId}";

  // ===== GET DTO LIST BY PARENT =====
  public static final String GET_UNITS_BY_SECTION = "/{sectionId}/units";
  public static final String GET_LESSONS_BY_UNIT = "/{unitId}/{userId}/lessons";
  public static final String GET_SECTIONS_BY_COURSE = "/{courseID}/sections";

  // !! TRANSACTIONAL !!//
  public static final String GET_EXERCISES_BY_LESSON = "{lessonId}/{userId}/exercises";

  // ===== GET DTO LIST FROM IDS =====
  public static final String GET_UNITS_FROM_IDS = "/ids";
  public static final String GET_LESSONS_FROM_IDS = "/ids";
  public static final String GET_SECTIONS_FROM_IDS = "/ids";

  // ===== GET IDS LIST BY PARENT =====
  public static final String GET_LESSON_IDS_BY_UNIT = GET_LESSONS_BY_UNIT + "/ids";
  public static final String GET_UNIT_IDS_BY_SECTION = GET_UNITS_BY_SECTION + "/ids";
  public static final String GET_SECTION_IDS_BY_COURSE = GET_SECTIONS_BY_COURSE + "/ids";

  // ----------------------------- POST REQUESTS AND MUTATIONS -----------------------------
  public static final String SUBMIT_EXERCSIE = "/submit";
  public static final String SUBMIT_COMPLETED_LESSON = "/completedLesson";
}
