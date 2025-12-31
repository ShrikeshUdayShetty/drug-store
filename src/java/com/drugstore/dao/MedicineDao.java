package com.drugstore.dao;

import com.drugstore.model.Medicine;
import com.drugstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineDao {

    private static final String INSERT_MEDICINE = "INSERT INTO medicines (vendor_id, name, description, manufacturing_date, expiry_date, price_per_unit, stock_quantity, image_url, discount_percentage) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_MEDICINE =
            "UPDATE medicines SET name = ?, description = ?, manufacturing_date = ?, expiry_date = ?, price_per_unit = ?, "
            + "stock_quantity = ?, image_url = ?, discount_percentage = ? WHERE id = ? AND vendor_id = ?";
    private static final String DELETE_MEDICINE = "DELETE FROM medicines WHERE id = ? AND vendor_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM medicines ORDER BY created_at DESC";
    private static final String SELECT_BY_VENDOR = "SELECT * FROM medicines WHERE vendor_id = ? ORDER BY created_at DESC";
    private static final String SELECT_BY_ID = "SELECT * FROM medicines WHERE id = ?";
    private static final String SELECT_BY_ID_FOR_VENDOR = "SELECT * FROM medicines WHERE id = ? AND vendor_id = ?";
    private static final String SEARCH = "SELECT * FROM medicines WHERE LOWER(name) LIKE ? ORDER BY created_at DESC";
    private static final String SELECT_FEATURED = "SELECT * FROM medicines ORDER BY created_at DESC LIMIT 6";
    private static final String COUNT_BY_VENDOR = "SELECT COUNT(*) AS total FROM medicines WHERE vendor_id = ?";

    public boolean addMedicine(Medicine medicine) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MEDICINE)) {
            ps.setInt(1, medicine.getVendorId());
            ps.setString(2, medicine.getName());
            ps.setString(3, medicine.getDescription());
            ps.setDate(4, medicine.getManufacturingDate());
            ps.setDate(5, medicine.getExpiryDate());
            ps.setDouble(6, medicine.getPricePerUnit());
            ps.setInt(7, medicine.getStockQuantity());
            ps.setString(8, medicine.getImageUrl());
            ps.setDouble(9, medicine.getDiscountPercentage());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Medicine> getAllMedicines() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = ps.executeQuery();
            return mapMedicines(rs);
        }
    }

    public List<Medicine> getMedicinesByVendor(int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_VENDOR)) {
            ps.setInt(1, vendorId);
            ResultSet rs = ps.executeQuery();
            return mapMedicines(rs);
        }
    }

    public Medicine findById(int id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMedicine(rs);
            }
            return null;
        }
    }

    public Medicine findByIdForVendor(int id, int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_FOR_VENDOR)) {
            ps.setInt(1, id);
            ps.setInt(2, vendorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMedicine(rs);
            }
            return null;
        }
    }

    public boolean updateMedicine(Medicine medicine) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MEDICINE)) {
            ps.setString(1, medicine.getName());
            ps.setString(2, medicine.getDescription());
            ps.setDate(3, medicine.getManufacturingDate());
            ps.setDate(4, medicine.getExpiryDate());
            ps.setDouble(5, medicine.getPricePerUnit());
            ps.setInt(6, medicine.getStockQuantity());
            ps.setString(7, medicine.getImageUrl());
            ps.setDouble(8, medicine.getDiscountPercentage());
            ps.setInt(9, medicine.getId());
            ps.setInt(10, medicine.getVendorId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMedicine(int id, int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MEDICINE)) {
            ps.setInt(1, id);
            ps.setInt(2, vendorId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Medicine> searchMedicines(String query) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH)) {
            ps.setString(1, "%" + query.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            return mapMedicines(rs);
        }
    }

    public List<Medicine> getFeaturedMedicines() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_FEATURED)) {
            ResultSet rs = ps.executeQuery();
            return mapMedicines(rs);
        }
    }

    public int countMedicinesByVendor(int vendorId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(COUNT_BY_VENDOR)) {
            ps.setInt(1, vendorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    private List<Medicine> mapMedicines(ResultSet rs) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        while (rs.next()) {
            medicines.add(mapMedicine(rs));
        }
        return medicines;
    }

    private Medicine mapMedicine(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setId(rs.getInt("id"));
        medicine.setVendorId(rs.getInt("vendor_id"));
        medicine.setName(rs.getString("name"));
        medicine.setDescription(rs.getString("description"));
        medicine.setManufacturingDate(rs.getDate("manufacturing_date"));
        medicine.setExpiryDate(rs.getDate("expiry_date"));
        medicine.setPricePerUnit(rs.getDouble("price_per_unit"));
        medicine.setStockQuantity(rs.getInt("stock_quantity"));
        medicine.setImageUrl(rs.getString("image_url"));
        medicine.setDiscountPercentage(rs.getDouble("discount_percentage"));
        medicine.setCreatedAt(rs.getTimestamp("created_at"));
        return medicine;
    }
}
