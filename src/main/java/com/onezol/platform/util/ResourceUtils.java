package com.onezol.platform.util;


import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceUtils extends org.springframework.util.ResourceUtils {

    /**
     * 从classpath/resource下加载文件
     *
     * @param path 文件路径(相对于classpath/resource), 如: /static/test.txt
     * @return 文件内容
     */
    public static String readFile(String path) throws IOException {
        path = path.startsWith("/") ? path.substring(1) : path;
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();
        return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    }
}
