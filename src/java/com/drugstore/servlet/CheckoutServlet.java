package com.drugstore.servlet;

import com.drugstore.dao.CartDao;
import com.drugstore.dao.OrderDao;
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

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/order/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final CartDao cartDao = new CartDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        try {
            List<CartItem> cartItems = cartDao.getCartItems(user.getId());
            if (cartItems.isEmpty()) {
                setMessage(session, "errorMessage", "Your cart is empty.", request);
                response.sendRedirect(request.getContextPath() + "/user/cart");
                return;
            }

            orderDao.createOrder(user, cartItems);
            cartDao.clearCart(user.getId());
            setMessage(session, "successMessage", "Order placed successfully.", request);
            response.sendRedirect(request.getContextPath() + "/user/orders");
        } catch (SQLException e) {
            setMessage(session, "errorMessage", "Unable to complete checkout: " + e.getMessage(), request);
            response.sendRedirect(request.getContextPath() + "/user/cart");
        }
    }

    private void setMessage(HttpSession session, String key, String value, HttpServletRequest request) {
        if (session == null) {
            session = request.getSession(true);
        }
        session.setAttribute(key, value);
    }
}
