<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.drugstore.model.CartItem" %>
<%
    List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
    if (cartItems == null) {
        cartItems = java.util.Collections.emptyList();
    }
    double total = 0;
    double discount = 0;
    for (CartItem item : cartItems) {
        double price = item.getMedicine().getPricePerUnit();
        double discountPercent = item.getMedicine().getDiscountPercentage();
        double lineTotal = price * item.getQuantity();
        total += lineTotal;
        discount += lineTotal * discountPercent / 100;
    }
    double payable = total - discount;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<jsp:include page="../includes/userHeader.jsp"/>
<main class="container" style="padding-bottom:3rem;">
    <h1 class="section-title">Shopping Cart</h1>
    <jsp:include page="../includes/alerts.jsp"/>

    <%
        if (cartItems.isEmpty()) {
    %>
    <div class="empty-state">Your cart is empty.</div>
    <%
        } else {
    %>
    <div class="grid" style="grid-template-columns: 2fr 1fr; gap:2rem; align-items:flex-start;">
        <div class="grid" style="gap:1rem;">
            <%
                for (CartItem item : cartItems) {
                    double price = item.getMedicine().getPricePerUnit();
                    double discountPercent = item.getMedicine().getDiscountPercentage();
                    double discountedPrice = price * (1 - discountPercent / 100);
            %>
            <div class="medicine-card" style="flex-direction:row; gap:1rem; align-items:center;">
                <img src="<%= (item.getMedicine().getImageUrl() != null && !item.getMedicine().getImageUrl().trim().isEmpty()) ? (item.getMedicine().getImageUrl().startsWith("/") ? request.getContextPath() + item.getMedicine().getImageUrl() : item.getMedicine().getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>" alt="<%= item.getMedicine().getName() %>" style="width:120px;height:120px;border-radius:14px;object-fit:cover;">
                <div style="flex:1;">
                    <h3><%= item.getMedicine().getName() %></h3>
                    <form action="<%= request.getContextPath() %>/cart/update" method="post" style="display:flex;flex-direction:column;gap:0.6rem;">
                        <input type="hidden" name="medicineId" value="<%= item.getMedicine().getId() %>">
                        <label style="color:var(--muted);font-size:0.9rem;display:flex;align-items:center;gap:0.5rem;">
                            Quantity
                            <input type="number" name="quantity" min="0" value="<%= item.getQuantity() %>" style="width:90px;">
                        </label>
                        <div style="display:flex;gap:0.6rem;align-items:center;flex-wrap:wrap;">
                            <p style="margin:0;color:var(--muted);">Unit price: ₹<%= String.format("%.2f", discountedPrice) %></p>
                            <p class="price" style="margin:0;">Line total: ₹<%= String.format("%.2f", discountedPrice * item.getQuantity()) %></p>
                        </div>
                        <div style="display:flex;gap:0.5rem;">
                            <button type="submit" class="btn btn-outline" style="flex:1;">Update</button>
                            <button type="submit" name="quantity" value="0" class="btn btn-text" style="flex:1;">Remove</button>
                        </div>
                    </form>
                </div>
            </div>
            <%
                }
            %>
        </div>
        <div class="medicine-card">
            <h3>Summary</h3>
            <p>Subtotal: ₹<%= String.format("%.2f", total) %></p>
            <p>Discounts: -₹<%= String.format("%.2f", discount) %></p>
            <hr>
            <p class="price">Payable: ₹<%= String.format("%.2f", payable) %></p>
            <form action="<%= request.getContextPath() %>/order/checkout" method="post">
                <button class="btn btn-primary" style="width:100%;">Checkout</button>
            </form>
        </div>
    </div>
    <%
        }
    %>
</main>
</body>
</html>
