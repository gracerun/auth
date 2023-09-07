package com.gracerun.security.authentication.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
public class ResponseUtil {

    public static void writeJson(ServletResponse response, String code, String msg) throws IOException {
        writeJson(response, code, msg, null);
    }

    public static void writeJson(ServletResponse response, String code, String msg, String data) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), Consts.UTF_8);
        outputStreamWriter.write(json.toJSONString());
        outputStreamWriter.flush();
    }
}
