package com.testingpractice.duoclonebackend.constants;

public class pathConstants {

    public static final String API_V1 = "/api";

    public static final String COURSES = API_V1 + "/courses";

    public static final String EXERCISES = API_V1 + "/exercises";
    public static final String SUBMIT_EXERCSIE = "/submit";


    public static final String UNITS = API_V1 + "/units";
    public static final String UNIT_LESSONS = "/{unitId}/lessons";
    public static final String LESSONS_UNITS_IDS = UNIT_LESSONS + "/ids";
    public static final String UNITS_FROM_IDS = "/ids";


    public static final String LESSONS = API_V1 + "/lessons";
    public static final String LESSONS_FROM_IDS = "/ids";
    public static final String LESSON_EXERCISES = "{lessonId}/{userId}/exercises";

    public static final String SECTIONS = API_V1 + "/sections";
    public static final String SECTIONS_FROM_IDS = "/ids";
    public static final String SECTION_UNITS = "/{sectionId}/units";
    public static final String SECTION_UNITS_IDS = SECTION_UNITS + "/ids";
    public static final String SECTIONS_FROM_COURSE_ID = SECTION_UNITS + "/ids";
}

