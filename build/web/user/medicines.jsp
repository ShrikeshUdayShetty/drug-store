<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.drugstore.model.Medicine" %>
<%
    List<Medicine> medicines = (List<Medicine>) request.getAttribute("medicines");
    if (medicines == null) {
        medicines = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Medicines</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<jsp:include page="../includes/userHeader.jsp"/>
<jsp:include page="../includes/alerts.jsp"/>
<main class="container" style="padding-bottom:3rem;">
    <h1 class="section-title">All Medicines</h1>
    <div class="grid" style="grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));">
        <%
            if (medicines.isEmpty()) {
        %>
        <div class="empty-state">No medicines available yet.</div>
        <%
            } else {
                for (Medicine medicine : medicines) {
        %>
        <form action="<%= request.getContextPath() %>/cart/add" method="post" class="medicine-card purchase-form">
            <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
            <img src="<%= (medicine.getImageUrl() != null && !medicine.getImageUrl().trim().isEmpty()) ? (medicine.getImageUrl().startsWith("/") ? request.getContextPath() + medicine.getImageUrl() : medicine.getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>" alt="<%= medicine.getName() %>" class="medicine-image">
            <h3><%= medicine.getName() %></h3>
            <p style="color:var(--muted);"><%= medicine.getDescription() != null ? medicine.getDescription() : "" %></p>
            <div class="price-row">
                <span class="price">₹<%= medicine.getPricePerUnit() %></span>
                <span class="discount"><%= medicine.getDiscountPercentage() %>% off</span>
            </div>
            <div class="form-group" style="margin-top:0.5rem;">
                <label for="med-qty-<%= medicine.getId() %>">Quantity</label>
                <input type="number" id="med-qty-<%= medicine.getId() %>" name="quantity" min="1" value="1" required>
            </div>
            <div class="card-actions">
                <button type="submit" class="btn btn-outline" style="flex:1;">Add to Cart</button>
                <button type="submit" formaction="<%= request.getContextPath() %>/order/buy-now" class="btn btn-primary" style="flex:1;">Buy</button>
            </div>
        </form>
        <%
                }
            }
        %>
    </div>
</main>
</body>
</html>
