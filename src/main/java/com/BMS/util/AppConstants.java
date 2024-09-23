package com.BMS.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class AppConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2";
//    public static final Long ADMIN_ID = 101L;
//    public static final Long USER_ID = 102L;
//    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
//    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/register/**", "/api/login" };
    public static final String[] USER_URLS = { "/api/public/**" };
    public static final String[] ADMIN_URLS = { "/api/admin/**" };
    public static final int COOKIE_MAX_AGE = 60 * 60 * 8; // 1 day
    public static final String COOKIE_PATH = "/";
    public static final String COOKIE_NAME = "sessionId";
    public static final long SCHEDULED_CLEAN_TIME_FOR_COOKIES = 60 * 60 * 1000;


    // Utility method to get session ID from the cookie
    public static String getSessionIdFromCookie(HttpServletRequest request) {
        if (request != null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (AppConstants.COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
