package com.testingpractice.duoclonebackend.utils;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

//TODO Add SetSecure true for prod



public class AuthCookieUtil {

    private static final String COOKIE_NAME = "jwt";
    private static final String PATH = "/";

    // create + attach cookie
    public static void addJwtCookie(HttpServletResponse response, String jwt, int maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, jwt)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(PATH)
                .maxAge(maxAgeSeconds)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // delete + attach cookie
    public static void clearJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(PATH)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // read cookie
    public static @Nullable String getJwtFromCookies(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if (COOKIE_NAME.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}