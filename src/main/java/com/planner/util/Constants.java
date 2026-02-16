package com.planner.util;

public class Constants {
    public static final String JWT_SECRET = "${app.jwt.secret}";
    public static final long JWT_EXPIRATION = 86400000;
    
    public static final String API_PREFIX = "/api";
    public static final String AUTH_PREFIX = API_PREFIX + "/auth";
    public static final String TODO_PREFIX = API_PREFIX + "/todos";
    public static final String TIMETABLE_PREFIX = API_PREFIX + "/timetable";
    public static final String PROGRESS_PREFIX = API_PREFIX + "/progress";
    public static final String DASHBOARD_PREFIX = API_PREFIX + "/dashboard";
}