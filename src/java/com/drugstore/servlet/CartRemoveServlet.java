package com.drugstore.servlet;

import com.drugstore.dao.CartDao;
import com.drugstore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CartRemoveServlet", urlPatterns = {"/cart/remove"})
public class CartRemoveServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        String medicineIdParam = request.getParameter("medicineId");
        if (isBlank(medicineIdParam)) {
            setMessage(session, request, "errorMessage", "Invalid request.");
            response.sendRedirect(request.getContextPath() + "/user/cart");
            return;
        }

        try {
            int medicineId = Integer.parseInt(medicineIdParam);
            cartDao.removeItem(user.getId(), medicineId);
            setMessage(session, request, "successMessage", "Item removed from cart.");
        } catch (NumberFormatException e) {
            setMessage(session, request, "errorMessage", "Invalid medicine id.");
        } catch (SQLException e) {
            setMessage(session, request, "errorMessage", "Unable to remove item: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/user/cart");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setMessage(HttpSession session, HttpServletRequest request, String key, String value) {
        if (session == null) {
            session = request.getSession(true);
        }
        session.setAttribute(key, value);
    }
}
