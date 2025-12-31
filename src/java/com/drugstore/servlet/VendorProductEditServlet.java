package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
import com.drugstore.model.Medicine;
import com.drugstore.model.Vendor;
import com.drugstore.util.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "VendorProductEditServlet", urlPatterns = {"/vendor/products/edit"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class VendorProductEditServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        String idParam = request.getParameter("medicineId");
        if (isBlank(idParam)) {
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

        Medicine existing;
        try {
            existing = medicineDao.findByIdForVendor(medicineId, vendor.getId());
            if (existing == null) {
                session.setAttribute("errorMessage", "Product not found or you do not have access to it.");
                response.sendRedirect(request.getContextPath() + "/vendor/products");
                return;
            }
            request.setAttribute("medicine", existing);
            request.getRequestDispatcher("/vendor/productForm.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Unable to load product for editing.");
            response.sendRedirect(request.getContextPath() + "/vendor/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Vendor vendor = session != null ? (Vendor) session.getAttribute("vendor") : null;

        if (vendor == null) {
            response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
            return;
        }

        String idParam = request.getParameter("medicineId");
        if (isBlank(idParam)) {
            session.setAttribute("errorMessage", "Missing product ID.");
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

        Medicine existing;
        try {
            existing = medicineDao.findByIdForVendor(medicineId, vendor.getId());
            if (existing == null) {
                session.setAttribute("errorMessage", "Product not found or you do not have access to it.");
                response.sendRedirect(request.getContextPath() + "/vendor/products");
                return;
            }
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Unable to load product for editing.");
            response.sendRedirect(request.getContextPath() + "/vendor/products");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceParam = request.getParameter("price");
        String stockParam = request.getParameter("stock");
        String discountParam = request.getParameter("discount");
        String imageUrl = request.getParameter("imageUrl");
        String manufacturingParam = request.getParameter("manufacturingDate");
        String expiryParam = request.getParameter("expiryDate");

        if (isBlank(name) || isBlank(priceParam) || isBlank(stockParam) || isBlank(manufacturingParam) || isBlank(expiryParam)) {
            session.setAttribute("errorMessage", "Name, price, stock and dates are required.");
            response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
            return;
        }

        double price;
        int stock;
        double discount = 0;
        try {
            price = Double.parseDouble(priceParam);
            stock = Integer.parseInt(stockParam);
            if (!isBlank(discountParam)) {
                discount = Double.parseDouble(discountParam);
                if (discount < 0) {
                    discount = 0;
                }
            }
            if (price < 0 || stock < 0) {
                session.setAttribute("errorMessage", "Price and stock must be positive values.");
                response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
                return;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Please enter valid numeric values for price, stock, and discount.");
            response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
            return;
        }

        Date manufacturingDate = parseDate(manufacturingParam);
        Date expiryDate = parseDate(expiryParam);
        if (manufacturingDate == null || expiryDate == null) {
            session.setAttribute("errorMessage", "Please provide valid dates in YYYY-MM-DD format.");
            response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
            return;
        }

        String uploadedImageUrl = null;
        try {
            uploadedImageUrl = UploadUtil.storeVendorProductImage(request, request.getPart("imageFile"));
        } catch (IOException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
            return;
        }

        Medicine medicine = new Medicine();
        medicine.setId(medicineId);
        medicine.setVendorId(vendor.getId());
        medicine.setName(name.trim());
        medicine.setDescription(!isBlank(description) ? description.trim() : null);
        medicine.setPricePerUnit(price);
        medicine.setStockQuantity(stock);
        medicine.setDiscountPercentage(discount);
        medicine.setImageUrl(determineImagePath(imageUrl, uploadedImageUrl, existing != null ? existing.getImageUrl() : null));
        medicine.setManufacturingDate(manufacturingDate);
        medicine.setExpiryDate(expiryDate);

        try {
            boolean updated = medicineDao.updateMedicine(medicine);
            if (updated) {
                session.setAttribute("successMessage", "Medicine updated successfully.");
            } else {
                session.setAttribute("errorMessage", "Unable to update medicine. Please try again.");
            }
            response.sendRedirect(request.getContextPath() + "/vendor/products");
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Server error while updating medicine.");
            response.sendRedirect(request.getContextPath() + "/vendor/products/edit?medicineId=" + medicineId);
        }
    }

    private Date parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return Date.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String determineImagePath(String imageUrl, String uploadedImageUrl, String fallback) {
        if (uploadedImageUrl != null) {
            return uploadedImageUrl;
        }
        if (!isBlank(imageUrl)) {
            return imageUrl.trim();
        }
        return fallback;
    }
}
