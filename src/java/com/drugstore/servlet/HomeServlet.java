package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
import com.drugstore.model.Medicine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Medicine> featured = medicineDao.getFeaturedMedicines();
            request.setAttribute("featuredMedicines", featured);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load medicines at the moment.");
        }
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }
}
