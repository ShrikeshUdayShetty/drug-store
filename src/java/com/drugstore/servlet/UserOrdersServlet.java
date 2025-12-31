package com.drugstore.servlet;

import com.drugstore.dao.OrderDao;
import com.drugstore.model.Order;
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

@WebServlet(name = "UserOrdersServlet", urlPatterns = {"/user/orders"})
public class UserOrdersServlet extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        try {
            List<Order> orders = orderDao.getOrdersByUser(user.getId());
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load orders: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/orders.jsp").forward(request, response);
    }
}
