package com.hmpr.woofy.auth.povider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

/**
 * 쿠키를 처리하는 유틸리티 클래스
 */
@Component
public class CookieProvider {

    /**
     * 쿠키에 액세스 토큰 저장
     *
     * @param response    HTTP 응답
     * @param accessToken 추가할 액세스 토큰
     */
    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("Authorization", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 쿠키에 내부에 액세스 토큰 값 조회
     *
     * @param request HTTP 요청
     * @return 액세스 토큰 값, 없다면 null
     */
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "Authorization");
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 쿠키 내부에 액세스 토큰 삭제
     *
     * @param response HTTP 응답
     */
    public void deleteAccessTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
