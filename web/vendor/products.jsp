<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
<%@ page import="com.drugstore.model.Medicine" %>
<%@ page import="java.util.List" %>
<%
    Vendor vendor = (Vendor) session.getAttribute("vendor");
    if (vendor == null) {
        response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
    <jsp:include page="../includes/vendorHeader.jsp"/>

    <main class="container" style="padding-bottom:3rem;">
        <jsp:include page="../includes/alerts.jsp"/>

        <header style="display:flex;justify-content:space-between;align-items:center;margin-bottom:2rem;gap:1rem;flex-wrap:wrap;">
            <div>
                <h1 class="section-title" style="margin:0;">Manage Products</h1>
                <p class="muted" style="margin:0;">Maintain your medicine catalogue and keep stock up to date.</p>
            </div>
            <a href="<%= request.getContextPath() %>/vendor/products/add" class="btn btn-primary">Add New Product</a>
        </header>

        <%
            List<Medicine> medicines = (List<Medicine>) request.getAttribute("medicines");
            boolean hasProducts = medicines != null && !medicines.isEmpty();
        %>

        <%
            if (!hasProducts) {
        %>
        <div class="empty-state" style="text-align:center;background:#fff;border-radius:16px;padding:2.5rem;margin-top:2rem;box-shadow:0 18px 36px rgba(31,47,70,0.08);">
            <img src="<%= request.getContextPath() %>/assets/images/empty-box.svg" alt="No products" style="max-width:180px;margin:0 auto 1.5rem;display:block;">
            <h2 style="margin-bottom:0.5rem;">No products yet</h2>
            <p class="muted" style="margin-bottom:1.5rem;">Start by adding your first medicine to the catalogue.</p>
            <a href="<%= request.getContextPath() %>/vendor/products/add" class="btn btn-primary">Add New Product</a>
        </div>
        <%
            } else {
        %>

        <div style="display:grid;gap:1.5rem;">
            <%
                for (Medicine medicine : medicines) {
            %>
            <article class="medicine-card" style="display:grid;grid-template-columns:140px 1fr;gap:1.5rem;align-items:center;">
                <div>
                    <img src="<%= (medicine.getImageUrl() != null && !medicine.getImageUrl().trim().isEmpty()) ? (medicine.getImageUrl().startsWith("/") ? request.getContextPath() + medicine.getImageUrl() : medicine.getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>"
                         alt="<%= medicine.getName() %>" style="width:140px;height:140px;object-fit:cover;border-radius:14px;">
                </div>
                <div style="display:grid;gap:0.75rem;">
                    <div style="display:flex;justify-content:space-between;align-items:flex-start;gap:1rem;flex-wrap:wrap;">
                        <div>
                            <h2 style="margin:0;font-size:1.35rem;"><%= medicine.getName() %></h2>
                            <p class="muted" style="margin:0;max-width:560px;">
                                <%= medicine.getDescription() != null ? medicine.getDescription() : "No description provided." %>
                            </p>
                        </div>
                        <div style="text-align:right;">
                            <p style="margin:0;font-size:1.5rem;font-weight:700;">₹<%= String.format("%.2f", medicine.getPricePerUnit()) %></p>
                            <p class="muted" style="margin:0;">Stock: <%= medicine.getStockQuantity() %> units</p>
                        </div>
                    </div>

                    <div style="display:flex;flex-wrap:wrap;gap:1.5rem;font-size:0.95rem;color:var(--muted);">
                        <span><strong>Manufactured:</strong> <%= medicine.getManufacturingDate() %></span>
                        <span><strong>Expires:</strong> <%= medicine.getExpiryDate() %></span>
                        <span><strong>Discount:</strong> <%= medicine.getDiscountPercentage() > 0 ? String.format("%.0f%%", medicine.getDiscountPercentage()) : "—" %></span>
                    </div>

                    <div style="display:flex;gap:1rem;flex-wrap:wrap;align-items:center;">
                        <a href="<%= request.getContextPath() %>/vendor/products/edit?medicineId=<%= medicine.getId() %>" class="btn btn-outline">Edit</a>
                        <form action="<%= request.getContextPath() %>/vendor/products/delete" method="post" onsubmit="return confirm('Remove this medicine from the catalogue?');">
                            <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
                            <button type="submit" class="btn btn-text" style="color:#dc2626;">Delete</button>
                        </form>
                    </div>
                </div>
            </article>
            <%
                }
            %>
        </div>

        <%
            }
        %>
    </main>
</body>
</html>
