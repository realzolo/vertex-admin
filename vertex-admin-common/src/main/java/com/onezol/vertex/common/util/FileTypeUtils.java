package com.onezol.vertex.common.util;

import com.onezol.vertex.common.constant.FileType;
import org.springframework.web.multipart.MultipartFile;

public class FileTypeUtils {
    /**
     * 获取文件类型
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getFileType(MultipartFile file) {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (StringUtils.isNotEmpty(contentType)) {
            if (contentType.startsWith("image/") || isImageByExtension(originalFilename)) {
                return FileType.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return FileType.VIDEO;
            } else if (contentType.startsWith("audio/")) {
                return FileType.AUDIO;
            } else if (isDocumentType(contentType) || isDocumentByExtension(originalFilename)) {
                return FileType.DOCUMENT;
            }
        }

        return FileType.OTHERS;
    }


    /**
     * 判断文件是否为图片
     *
     * @param filename 文件名
     * @return 是否为图片
     */
    public static boolean isImageByExtension(String filename) {
        String extension = getFileExtension(filename);

        return extension != null && (extension.equalsIgnoreCase("jpg")
                || extension.equalsIgnoreCase("jpeg")
                || extension.equalsIgnoreCase("png")
                || extension.equalsIgnoreCase("gif")
                || extension.equalsIgnoreCase("bmp")
        );
    }

    /**
     * 判断文件是否为文档
     *
     * @param filename 文件名
     * @return 是否为文档
     */
    public static boolean isDocumentByExtension(String filename) {
        String extension = getFileExtension(filename);

        return extension != null && (extension.equalsIgnoreCase("doc")
                || extension.equalsIgnoreCase("docx")
                || extension.equalsIgnoreCase("ppt")
                || extension.equalsIgnoreCase("pptx")
                || extension.equalsIgnoreCase("pdf")
        );
    }

    /**
     * 判断文件是否为文档
     *
     * @param contentType 文件类型
     * @return 是否为文档
     */
    public static boolean isDocumentType(String contentType) {
        return contentType.equals("application/pdf")
                || contentType.equals("application/msword")
                || contentType.equals("application/vnd.ms-powerpoint")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 文件扩展名
     */
    public static String getFileExtension(String filename) {
        if (filename != null && filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {
            return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }
        return null;
    }
}
