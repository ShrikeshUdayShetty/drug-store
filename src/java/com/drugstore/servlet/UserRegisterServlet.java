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

@WebServlet(name = "UserRegisterServlet", urlPatterns = {"/user/register"})
public class UserRegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (isBlank(fullName) || isBlank(email) || isBlank(password)) {
            setErrorAndRedirect(request, response, "Full name, email and password are required.", "/user/register.jsp");
            return;
        }

        try {
            if (userDao.findByEmail(email) != null) {
                setErrorAndRedirect(request, response, "Email already registered. Please login.", "/user/login.jsp");
                return;
            }

            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPasswordHash(PasswordUtil.hashPassword(password));
            user.setPhone(phone);
            user.setAddress(address);

            if (userDao.registerUser(user)) {
                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "Registration successful. Please login.");
                response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            } else {
                setErrorAndRedirect(request, response, "Could not register user. Try again later.", "/user/register.jsp");
            }
        } catch (SQLException e) {
            setErrorAndRedirect(request, response, "Server error: " + e.getMessage(), "/user/register.jsp");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String message, String path) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", message);
        response.sendRedirect(request.getContextPath() + path);
    }
}
