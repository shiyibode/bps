package org.nmgns.bps.system.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

// 自定义返回json，当用户未登录访问资源时，不跳转到默认的登录页面（springsecurity默认跳转登录页面），而是返回自定义的json
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.set("success", false);
        json.set("data", new UnAuthenticationClass());

//        response.setContentType("text/html;charset=utf-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        out.write(JSONUtil.toJsonStr(json));



//        out.write("user haven't authenticated");
        out.flush();
        out.close();
    }

    // 私有内部类
    private static class UnAuthenticationClass {
        public UnAuthenticationClass() {
            loggedIn = false;
        }
        private final Boolean loggedIn;

        public Boolean getLoggedIn() {
            return loggedIn;
        }
    }
}
