<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
<%@ page import="com.drugstore.model.Medicine" %>
<%
    Vendor vendor = (Vendor) session.getAttribute("vendor");
    if (vendor == null) {
        response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
        return;
    }
    Medicine medicine = (Medicine) request.getAttribute("medicine");
    boolean editing = medicine != null;
    String formAction = editing ? request.getContextPath() + "/vendor/products/edit" : request.getContextPath() + "/vendor/products/add";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Medicine</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    <style>
        body {
            background: linear-gradient(180deg, rgba(241,245,249,0.7) 0%, #ffffff 65%);
        }
        .form-shell {
            max-width: 920px;
            margin: 2.5rem auto 4rem;
            background: #ffffff;
            border-radius: 24px;
            padding: 2.75rem;
            box-shadow: 0 24px 60px rgba(15, 23, 42, 0.12);
            display: grid;
            gap: 2rem;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 1.5rem 2rem;
        }
        .form-group label {
            display: block;
            font-weight: 600;
            margin-bottom: 0.35rem;
            color: var(--text-strong,#1f2f46);
        }
        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 0.75rem 1rem;
            border-radius: 12px;
            border: 1px solid rgba(15, 23, 42, 0.16);
            background: rgba(241,245,249,0.6);
            font-size: 0.95rem;
            transition: border 0.2s ease, box-shadow 0.2s ease;
        }
        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: rgba(59,130,246,0.6);
            box-shadow: 0 0 0 4px rgba(59,130,246,0.08);
        }
        textarea {
            resize: vertical;
            min-height: 120px;
        }
        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
        }
        .section-label {
            font-size: 0.8rem;
            text-transform: uppercase;
            letter-spacing: 0.12em;
            font-weight: 700;
            color: rgba(15,23,42,0.45);
        }
    </style>
</head>
<body>
    <jsp:include page="../includes/vendorHeader.jsp"/>

    <main class="container" style="padding-bottom:4rem;">
        <jsp:include page="../includes/alerts.jsp"/>
        <div class="form-shell">
            <header style="display:flex;justify-content:space-between;align-items:flex-start;gap:1rem;">
                <div>
                    <p class="section-label"><%= editing ? "Update product" : "Create product" %></p>
                    <h1 class="section-title" style="margin:0.5rem 0 0;"><%= editing ? "Edit medicine" : "Add new medicine" %></h1>
                    <p class="muted" style="margin:0;max-width:520px;">Provide clear details so customers can understand the benefits and dosage at a glance.</p>
                </div>
                <a href="<%= request.getContextPath() %>/vendor/products" class="btn btn-outline">Back to products</a>
            </header>

            <form action="<%= formAction %>" method="post" class="grid" style="gap:2rem;">
                <% if (editing) { %>
                    <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
                <% } %>
                <section class="grid" style="gap:1.5rem;">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="name">Medicine name *</label>
                            <input type="text" id="name" name="name" placeholder="e.g., Azithromycin 500mg" value="<%= editing ? medicine.getName() : "" %>" required>
                        </div>
                        <div class="form-group">
                            <label for="price">Price per unit (₹) *</label>
                            <input type="number" step="0.01" min="0" id="price" name="price" placeholder="0.00" value="<%= editing ? String.format("%.2f", medicine.getPricePerUnit()) : "" %>" required>
                        </div>
                        <div class="form-group">
                            <label for="stock">Stock quantity *</label>
                            <input type="number" min="0" id="stock" name="stock" placeholder="0" value="<%= editing ? medicine.getStockQuantity() : "" %>" required>
                        </div>
                        <div class="form-group">
                            <label for="discount">Discount (%)</label>
                            <input type="number" min="0" max="90" step="0.5" id="discount" name="discount" placeholder="0" value="<%= editing ? String.format("%.2f", medicine.getDiscountPercentage()) : "" %>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" placeholder="Add highlights, ingredients, dosage instructions, or storage information."><%= editing && medicine.getDescription() != null ? medicine.getDescription() : "" %></textarea>
                    </div>
                </section>

                <section class="grid" style="gap:1.5rem;">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="manufacturingDate">Manufacturing date *</label>
                            <input type="date" id="manufacturingDate" name="manufacturingDate" value="<%= editing && medicine.getManufacturingDate() != null ? medicine.getManufacturingDate().toString() : "" %>" required>
                        </div>
                        <div class="form-group">
                            <label for="expiryDate">Expiry date *</label>
                            <input type="date" id="expiryDate" name="expiryDate" value="<%= editing && medicine.getExpiryDate() != null ? medicine.getExpiryDate().toString() : "" %>" required>
                        </div>
                        <div class="form-group">
                            <label for="imageUrl">Image URL</label>
                            <input type="text" id="imageUrl" name="imageUrl" placeholder="https://..." value="<%= editing && medicine.getImageUrl() != null ? medicine.getImageUrl() : "" %>">
                        </div>
                    </div>
                </section>

                <div class="form-actions">
                    <a href="<%= request.getContextPath() %>/vendor/products" class="btn btn-text" style="padding:0;">Cancel</a>
                    <button type="submit" class="btn btn-primary"><%= editing ? "Update medicine" : "Publish medicine" %></button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>
