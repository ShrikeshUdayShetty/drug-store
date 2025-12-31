package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
import com.drugstore.model.Medicine;
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

@WebServlet(name = "VendorProductsServlet", urlPatterns = {"/vendor/products"})
public class VendorProductsServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        try {
            List<Medicine> medicines = medicineDao.getMedicinesByVendor(vendor.getId());
            request.setAttribute("medicines", medicines != null ? medicines : Collections.<Medicine>emptyList());
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Unable to load products right now. Please try again later.");
            request.setAttribute("medicines", Collections.emptyList());
        }

        request.getRequestDispatcher("/vendor/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
