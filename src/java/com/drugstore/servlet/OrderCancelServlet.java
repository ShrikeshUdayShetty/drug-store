package com.drugstore.servlet;

import com.drugstore.dao.OrderDao;
import com.drugstore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "OrderCancelServlet", urlPatterns = {"/order/cancel"})
public class OrderCancelServlet extends HttpServlet {

    private static final int CANCEL_WINDOW_MINUTES = 10;
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        if (isBlank(orderIdParam)) {
            setMessage(session, request, "errorMessage", "Invalid request.");
            response.sendRedirect(request.getContextPath() + "/user/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            boolean cancelled = orderDao.cancelOrder(orderId, user.getId(), CANCEL_WINDOW_MINUTES);
            if (cancelled) {
                setMessage(session, request, "successMessage", "Order cancelled successfully.");
            } else {
                setMessage(session, request, "errorMessage", "Unable to cancel order. It may be older than 10 minutes or already processed.");
            }
        } catch (NumberFormatException e) {
            setMessage(session, request, "errorMessage", "Invalid order id.");
        } catch (SQLException e) {
            setMessage(session, request, "errorMessage", "Error cancelling order: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/user/orders");
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
