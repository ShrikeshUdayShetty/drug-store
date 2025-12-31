package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
import com.drugstore.dao.OrderDao;
import com.drugstore.model.Vendor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "VendorDashboardServlet", urlPatterns = {"/vendor/dashboard"})
public class VendorDashboardServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        int totalProducts = 0;
        int pendingOrders = 0;
        double totalSales = 0;

        try {
            totalProducts = medicineDao.countMedicinesByVendor(vendor.getId());
            pendingOrders = orderDao.countPendingOrdersForVendor(vendor.getId());
            totalSales = orderDao.getTotalSalesForVendor(vendor.getId());
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load dashboard metrics: " + e.getMessage());
        }

        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("totalSales", totalSales);

        request.getRequestDispatcher("/vendor/dashboard.jsp").forward(request, response);
    }
}
