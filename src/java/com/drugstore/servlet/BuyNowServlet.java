package com.drugstore.servlet;

import com.drugstore.dao.OrderDao;
import com.drugstore.dao.MedicineDao;
import com.drugstore.model.CartItem;
import com.drugstore.model.Medicine;
import com.drugstore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

@WebServlet(name = "BuyNowServlet", urlPatterns = {"/order/buy-now"})
public class BuyNowServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        String medicineIdParam = request.getParameter("medicineId");
        String quantityParam = request.getParameter("quantity");

        if (isBlank(medicineIdParam) || isBlank(quantityParam)) {
            setErrorAndRedirect(session, response, request, "Invalid request. Please try again.");
            return;
        }

        try {
            int medicineId = Integer.parseInt(medicineIdParam);
            int quantity = Math.max(1, Integer.parseInt(quantityParam));

            Medicine medicine = medicineDao.findById(medicineId);
            if (medicine == null) {
                setErrorAndRedirect(session, response, request, "Medicine not found.");
                return;
            }

            CartItem item = new CartItem();
            item.setUserId(user.getId());
            item.setMedicine(medicine);
            item.setQuantity(quantity);

            orderDao.createOrder(user, Collections.singletonList(item));
            session.setAttribute("successMessage", "Order placed successfully.");
            response.sendRedirect(request.getContextPath() + "/user/orders");
        } catch (NumberFormatException e) {
            setErrorAndRedirect(session, response, request, "Invalid quantity.");
        } catch (SQLException e) {
            setErrorAndRedirect(session, response, request, "Unable to process order: " + e.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setErrorAndRedirect(HttpSession session, HttpServletResponse response,
                                     HttpServletRequest request, String message) throws IOException {
        if (session == null) {
            session = request.getSession(true);
        }
        session.setAttribute("errorMessage", message);
        response.sendRedirect(request.getHeader("referer") != null ? request.getHeader("referer")
                : request.getContextPath() + "/home");
    }
}
