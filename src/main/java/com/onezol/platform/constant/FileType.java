package com.onezol.platform.constant;

import java.util.HashMap;
import java.util.Map;

public interface FileType {
    String IMAGE = "image";
    String VIDEO = "video";
    String AUDIO = "audio";
    String DOCUMENT = "document";
    String OTHERS = "others";

    Map<String, String> UPLOAD_PATH_MAP = new HashMap<String, String>() {{
        put(IMAGE, "/image");
        put(VIDEO, "/video");
        put(AUDIO, "/audio");
        put(DOCUMENT, "/document");
        put(OTHERS, "/others");
    }};
}
