package com.pccc.team.auth.auth_service.utils;

import com.pccc.team.auth.auth_service.entity.LoginResult;
import com.pccc.team.auth.auth_service.entity.RestResult;
import com.pccc.team.auth.auth_service.enums.ResponseEnums;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerletResponse {
    public static void doResponse(HttpServletResponse response, ResponseEnums enums, LoginResult loginResult, String status, boolean success) throws IOException {
        RestResult<LoginResult> result = new RestResult<>(enums,loginResult);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    public static void doResponse(HttpServletResponse response, int httpStatus, ResponseEnums enums, String status, boolean success) throws IOException {
        RestResult<ResponseEnums> restResult = new RestResult<>(success,enums);
        restResult.setStatus(status);
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(restResult.toString());
    }
}
