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

@WebServlet(name = "CartAddServlet", urlPatterns = {"/cart/add"})
public class CartAddServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        int medicineId = Integer.parseInt(request.getParameter("medicineId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try {
            cartDao.addToCart(user.getId(), medicineId, quantity);
            session.setAttribute("successMessage", "Item added to cart.");
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Unable to add to cart: " + e.getMessage());
        }
        response.sendRedirect(request.getHeader("referer"));
    }
}
