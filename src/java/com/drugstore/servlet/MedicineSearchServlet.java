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

@WebServlet(name = "MedicineSearchServlet", urlPatterns = {"/medicines/search"})
public class MedicineSearchServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("q");
        List<Medicine> medicines;
        try {
            if (query == null || query.trim().isEmpty()) {
                medicines = medicineDao.getFeaturedMedicines();
            } else {
                medicines = medicineDao.searchMedicines(query.trim());
            }
            request.setAttribute("medicines", medicines);
            request.getRequestDispatcher("/user/components/medicineGrid.jsp").forward(request, response);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("<div class='empty-state'>Unable to search medicines.</div>");
        }
    }
}
