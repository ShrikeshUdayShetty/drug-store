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

@WebServlet(name = "TotalSpendServlet", urlPatterns = {"/user/spend"})
public class TotalSpendServlet extends HttpServlet {

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
            double totalSpent = orderDao.getTotalSpent(user.getId());
            request.setAttribute("totalSpent", totalSpent);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to fetch spend summary: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/spend.jsp").forward(request, response);
    }
}
