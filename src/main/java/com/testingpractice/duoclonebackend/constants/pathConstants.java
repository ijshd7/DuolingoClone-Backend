package com.testingpractice.duoclonebackend.constants;

public class pathConstants {

    public static final String API_V1 = "/api";

    public static final String COURSES = API_V1 + "/courses";
    public static final String COURSE_UNITS = "/{courseId}/units";

    public static final String UNITS = API_V1 + "/units";
    public static final String UNIT_LESSONS = UNITS + "/{unitId}/lessons";
}

