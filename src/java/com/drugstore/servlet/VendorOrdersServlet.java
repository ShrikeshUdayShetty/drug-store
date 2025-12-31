package com.drugstore.servlet;

import com.drugstore.dao.OrderDao;
import com.drugstore.model.Order;
import com.drugstore.model.Vendor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "VendorOrdersServlet", urlPatterns = {"/vendor/orders"})
public class VendorOrdersServlet extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        try {
            List<Order> orders = orderDao.getOrdersForVendor(vendor.getId());
            request.setAttribute("orders", orders != null ? orders : Collections.emptyList());
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Unable to load orders right now. Please try again later.");
            request.setAttribute("orders", Collections.emptyList());
        }

        request.getRequestDispatcher("/vendor/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid order selection.");
            response.sendRedirect(request.getContextPath() + "/vendor/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            boolean updated = orderDao.markOrderDelivered(orderId, vendor.getId());
            if (updated) {
                session.setAttribute("successMessage", "Order marked as delivered.");
            } else {
                session.setAttribute("errorMessage", "Unable to mark this order as delivered.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid order ID.");
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Server error while updating order status.");
        }

        response.sendRedirect(request.getContextPath() + "/vendor/orders");
    }
}
