package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        /* ===== manager画面 ===== */
        if (uri.startsWith("/manager")) {

            if (uri.equals("/manager/login") || uri.equals("/manager/logout")) {
                return true;
            }

            if (session == null || session.getAttribute("loginManager") == null) {
                response.sendRedirect("/manager/login");
                return false;
            }
        }

        /* ===== customer画面 ===== */
        if (uri.startsWith("/customer")) {

            if (uri.equals("/customer/login") || uri.equals("/customer/logout")) {
                return true;
            }

            // customer or manager どちらも未ログインはNG
            if (session == null ||
               (session.getAttribute("loginCustomer") == null
             && session.getAttribute("loginManager") == null)) {

                response.sendRedirect("/customer/login");
                return false;
            }
        }

        /* ===== customerログイン必須機能 ===== */
        if (uri.startsWith("/order")
         || uri.startsWith("/customer/mypage")) {

            if (session == null ||
               (session.getAttribute("loginCustomer") == null
             && session.getAttribute("loginManager") == null)) {

                response.sendRedirect("/customer/login");
                return false;
            }
        }

        /* ===== manager専用機能 ===== */
        if (uri.startsWith("/product")
         || uri.startsWith("/sales")) {

            if (session == null || session.getAttribute("loginManager") == null) {
                response.sendRedirect("/manager/login");
                return false;
            }
        }

        return true;
    }
}
