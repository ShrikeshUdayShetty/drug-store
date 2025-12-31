package com.drugstore.servlet;

import com.drugstore.dao.MedicineDao;
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
import java.util.List;

@WebServlet(name = "MedicineListServlet", urlPatterns = {"/user/medicines"})
public class MedicineListServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        try {
            List<Medicine> medicines = medicineDao.getAllMedicines();
            request.setAttribute("medicines", medicines);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load medicines: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/medicines.jsp").forward(request, response);
    }
}
