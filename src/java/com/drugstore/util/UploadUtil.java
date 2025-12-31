package com.drugstore.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

public final class UploadUtil {

    public static final String UPLOAD_DIRECTORY = "/uploads/vendor-products";
    public static final String MEDIA_PREFIX = "/media/vendor/";
    private static final String UPLOAD_PATH_ATTRIBUTE = "vendorProductUploadPath";

    private UploadUtil() {
    }

    public static Path getVendorProductUploadDirectory(ServletContext context) throws IOException {
        Object cached = context.getAttribute(UPLOAD_PATH_ATTRIBUTE);
        if (cached instanceof String) {
            Path cachedPath = Paths.get((String) cached);
            Files.createDirectories(cachedPath);
            return cachedPath;
        }

        Path root = resolveUploadRoot(context);
        Path vendorDir = root.resolve("vendor-products");
        Files.createDirectories(vendorDir);
        context.setAttribute(UPLOAD_PATH_ATTRIBUTE, vendorDir.toString());
        return vendorDir;
    }

    public static String storeVendorProductImage(HttpServletRequest request, Part imagePart) throws IOException {
        if (imagePart == null || imagePart.getSize() == 0 || isBlank(imagePart.getSubmittedFileName())) {
            return null;
        }

        String contentType = imagePart.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ENGLISH).startsWith("image/")) {
            throw new IOException("Unsupported image format. Please upload a JPG, PNG, WEBP, or GIF file.");
        }

        String originalFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        String extension = extractExtension(originalFileName);
        if (extension == null) {
            extension = mimeTypeToExtension(contentType);
        }
        if (extension == null) {
            extension = "png";
        }

        String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
        Path uploadDir = getVendorProductUploadDirectory(request.getServletContext());
        try (InputStream input = imagePart.getInputStream()) {
            Files.copy(input, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }

        return MEDIA_PREFIX + fileName;
    }

    public static Path resolveVendorProductMedia(ServletContext context, String mediaPath) throws IOException {
        if (isBlank(mediaPath) || !mediaPath.startsWith(MEDIA_PREFIX)) {
            return null;
        }
        String fileName = mediaPath.substring(MEDIA_PREFIX.length());
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return null;
        }
        Path current = getVendorProductUploadDirectory(context).resolve(fileName);
        if (Files.exists(current)) {
            return current;
        }

        String legacyRealPath = context.getRealPath(UPLOAD_DIRECTORY);
        if (legacyRealPath != null) {
            Path legacyPath = Paths.get(legacyRealPath).resolve(fileName);
            if (Files.exists(legacyPath)) {
                return legacyPath;
            }
        }

        Object tempDirAttr = context.getAttribute("javax.servlet.context.tempdir");
        if (tempDirAttr instanceof File) {
            Path tempPath = ((File) tempDirAttr).toPath().resolve("vendor-products").resolve(fileName);
            if (Files.exists(tempPath)) {
                return tempPath;
            }
        }

        return current;
    }

    private static Path resolveUploadRoot(ServletContext context) {
        String configured = context.getInitParameter("vendorUploadRoot");
        if (!isBlank(configured)) {
            return Paths.get(configured);
        }

        String systemProperty = System.getProperty("drugstore.upload.dir");
        if (!isBlank(systemProperty)) {
            return Paths.get(systemProperty);
        }

        String userHome = System.getProperty("user.home");
        if (!isBlank(userHome)) {
            return Paths.get(userHome, "drugstore-uploads");
        }

        Object tempDirAttr = context.getAttribute("javax.servlet.context.tempdir");
        if (tempDirAttr instanceof File) {
            return ((File) tempDirAttr).toPath().resolve("drugstore-uploads");
        }

        String tmp = System.getProperty("java.io.tmpdir");
        if (!isBlank(tmp)) {
            return Paths.get(tmp, "drugstore-uploads");
        }

        return Paths.get(".", "drugstore-uploads");
    }

    private static String extractExtension(String fileName) {
        if (isBlank(fileName) || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH);
    }

    private static String mimeTypeToExtension(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        switch (mimeType.toLowerCase(Locale.ENGLISH)) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/webp":
                return "webp";
            case "image/gif":
                return "gif";
            default:
                return null;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
