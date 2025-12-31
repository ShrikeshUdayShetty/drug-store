package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
import com.drugstore.model.Vendor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "VendorProductDeleteServlet", urlPatterns = {"/vendor/products/delete"})
public class VendorProductDeleteServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        String idParam = request.getParameter("medicineId");
        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid product selection.");
            response.sendRedirect(request.getContextPath() + "/vendor/products");
            return;
        }

        int medicineId;
        try {
            medicineId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid product ID.");
            response.sendRedirect(request.getContextPath() + "/vendor/products");
            return;
        }

        try {
            boolean removed = medicineDao.deleteMedicine(medicineId, vendor.getId());
            if (removed) {
                session.setAttribute("successMessage", "Medicine removed from catalogue.");
            } else {
                session.setAttribute("errorMessage", "Unable to delete this medicine.");
            }
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Server error while deleting medicine.");
        }

        response.sendRedirect(request.getContextPath() + "/vendor/products");
    }
}
