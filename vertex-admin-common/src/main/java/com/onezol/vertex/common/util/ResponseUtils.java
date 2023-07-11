package com.onezol.vertex.common.util;

import com.onezol.vertex.common.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    /**
     * 以JSON形式返回异常信息
     *
     * @param response  response
     * @param exception exception
     */
    public static void write(HttpServletResponse response, Exception exception) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", exception.getMessage());
        map.put("success", false);

        if (exception instanceof BusinessException) {
            BusinessException ex = (BusinessException) exception;
            map.put("code", ex.getCode());
        } else {
            map.put("code", 500);
        }

        String json = JsonUtils.mapToJson(map);

        doWrite(response, json);
    }

    /**
     * 以JSON形式返回信息
     *
     * @param response response
     */
    public static void write(HttpServletResponse response, String data) {
        doWrite(response, data);
    }

    /**
     * 使用response输出信息
     *
     * @param response response
     * @param json     json
     */
    private static void doWrite(HttpServletResponse response, String json) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
