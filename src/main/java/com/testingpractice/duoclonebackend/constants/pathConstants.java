package com.testingpractice.duoclonebackend.constants;

public class pathConstants {

    public static final String API_V1 = "/api";

    public static final String COURSES = API_V1 + "/courses";
    public static final String COURSE_UNITS = "/{courseId}/units";
    public static final String COURSE_UNITS_IDS = COURSE_UNITS + "/ids";

    public static final String UNITS = API_V1 + "/units";
    public static final String UNIT_LESSONS = "/{unitId}/lessons";
    public static final String LESSONS_UNITS_IDS = UNIT_LESSONS + "/ids";
    public static final String UNITS_FROM_IDS = "/ids";


    public static final String LESSONS = API_V1 + "/lessons";
    public static final String LESSONS_FROM_IDS = "/ids";

}

