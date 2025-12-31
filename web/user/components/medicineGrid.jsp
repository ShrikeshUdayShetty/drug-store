<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.drugstore.model.Medicine" %>
<%
    List<Medicine> medicines = (List<Medicine>) request.getAttribute("medicines");
    if (medicines == null) {
        medicines = java.util.Collections.emptyList();
    }
%>
<%
    if (medicines.isEmpty()) {
%>
    <div class="empty-state" style="grid-column: 1 / -1;">No medicines match your search.</div>
<%
    } else {
        for (Medicine medicine : medicines) {
%>
    <div class="medicine-card" data-name="<%= medicine.getName().toLowerCase() %>">
        <img src="<%= (medicine.getImageUrl() != null && !medicine.getImageUrl().trim().isEmpty()) ? (medicine.getImageUrl().startsWith("/") ? request.getContextPath() + medicine.getImageUrl() : medicine.getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>" alt="<%= medicine.getName() %>" class="medicine-image">
        <div>
            <h3><%= medicine.getName() %></h3>
            <p style="color:var(--muted);"><%= medicine.getDescription() != null ? medicine.getDescription() : "Medicine description coming soon." %></p>
        </div>
        <div class="price-row">
            <span class="price">₹<%= medicine.getPricePerUnit() %></span>
            <span class="discount"><%= medicine.getDiscountPercentage() %>% off</span>
        </div>
        <div class="quantity-picker">
            <label>Quantity</label>
            <input type="number" min="1" value="1" data-medicine-id="<%= medicine.getId() %>">
        </div>
        <div class="card-actions">
            <form action="<%= request.getContextPath() %>/cart/add" method="post" style="flex:1;" class="add-to-cart-form">
                <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
                <input type="hidden" name="quantity" value="1">
                <button type="submit" class="btn btn-outline" style="width:100%;">Add to Cart</button>
            </form>
            <form action="<%= request.getContextPath() %>/order/buy-now" method="post" style="flex:1;" class="buy-now-form">
                <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
                <input type="hidden" name="quantity" value="1">
                <button type="submit" class="btn btn-primary" style="width:100%;">Buy</button>
            </form>
        </div>
    </div>
<%
        }
    }
%>
