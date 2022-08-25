package com.admin.tool.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseWriter {
    public static void write(HttpServletResponse response, String msg, int code) throws IOException {
        write(response, msg, code, null);
    }

    public static void write(HttpServletResponse response, String msg, int code, String token) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);

        StringBuilder json = new StringBuilder();
        json.append("{\"code\" : " + code + " , \"msg\" : \"" + msg + "\"");
        if (token != null)
            json.append(",\"token\" : \"" + token + " \"");

        json.append("}");

        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }
}