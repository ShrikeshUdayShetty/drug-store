package com.drugstore.servlet;

import com.drugstore.dao.UserDao;
import com.drugstore.model.User;
import com.drugstore.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserLoginServlet", urlPatterns = {"/user/login"})
public class UserLoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if (isBlank(email) || isBlank(password)) {
            session.setAttribute("errorMessage", "Email and password are required.");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        try {
            User user = userDao.findByEmail(email);
            if (user != null && PasswordUtil.matches(password, user.getPasswordHash())) {
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                session.setAttribute("errorMessage", "Invalid credentials.");
                response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            }
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Server error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
