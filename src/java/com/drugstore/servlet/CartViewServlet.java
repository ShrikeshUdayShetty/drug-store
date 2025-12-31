package com.drugstore.servlet;

import com.drugstore.dao.CartDao;
import com.drugstore.model.CartItem;
import com.drugstore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CartViewServlet", urlPatterns = {"/user/cart"})
public class CartViewServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        try {
            List<CartItem> items = cartDao.getCartItems(user.getId());
            request.setAttribute("cartItems", items);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load cart: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/cart.jsp").forward(request, response);
    }
}
