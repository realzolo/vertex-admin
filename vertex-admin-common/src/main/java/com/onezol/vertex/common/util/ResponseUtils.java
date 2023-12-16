package com.onezol.vertex.common.util;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.record.GenericResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtils {

    /**
     * 以JSON形式返回异常信息
     *
     * @param response  response
     * @param exception exception
     */
    public static void write(HttpServletResponse response, Exception exception) {
        int code = 500;
        if (exception instanceof BusinessException) {
            code = ((BusinessException) exception).getCode();
        }

        GenericResponse<Void> genericResponse = GenericResponse.failure(code, exception.getMessage());
        String data = JsonUtils.toJson(genericResponse);

        doWrite(response, data);
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
     * @param data     data
     */
    private static void doWrite(HttpServletResponse response, String data) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
