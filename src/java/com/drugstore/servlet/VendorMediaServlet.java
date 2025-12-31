package com.drugstore.servlet;

import com.drugstore.util.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet(name = "VendorMediaServlet", urlPatterns = {"/media/vendor/*"})
public class VendorMediaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mediaPath = UploadUtil.MEDIA_PREFIX + pathInfo.substring(1);
        Path filePath = UploadUtil.resolveVendorProductMedia(getServletContext(), mediaPath);
        if (filePath == null || !Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(filePath));

        Files.copy(filePath, response.getOutputStream());
    }
}
