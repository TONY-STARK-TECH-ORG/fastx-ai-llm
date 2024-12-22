package com.fastx.ai.llm.web.config;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.web.controller.entity.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * @author stark
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String satoken = request.getHeader("satoken");

        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        if (StringUtils.isEmpty(satoken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().print(JSON.toJSONString(
                    Response.error("no satoken found!")
            ));
            response.getWriter().close();
            return false;
        }

        Object loginIdByToken = StpUtil.getLoginIdByToken(satoken);
        if (Objects.isNull(loginIdByToken)) {
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
