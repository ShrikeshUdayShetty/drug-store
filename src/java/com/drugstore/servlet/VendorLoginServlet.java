package com.drugstore.servlet;

import com.drugstore.dao.VendorDao;
import com.drugstore.model.Vendor;
import com.drugstore.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "VendorLoginServlet", urlPatterns = {"/vendor/login"})
public class VendorLoginServlet extends HttpServlet {

    private final VendorDao vendorDao = new VendorDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if (isBlank(email) || isBlank(password)) {
            session.setAttribute("errorMessage", "Email and password are required.");
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        try {
            Vendor vendor = vendorDao.findByEmail(email);
            if (vendor != null && PasswordUtil.matches(password, vendor.getPasswordHash())) {
                session.setAttribute("vendor", vendor);
                response.sendRedirect(request.getContextPath() + "/vendor/dashboard");
            } else {
                session.setAttribute("errorMessage", "Invalid email or password.");
                response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            }
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Server error. Please try again later.");
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            e.printStackTrace();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
