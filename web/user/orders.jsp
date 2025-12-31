<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.drugstore.model.Order" %>
<%@ page import="com.drugstore.model.OrderItem" %>
<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders == null) {
        orders = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Orders</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<jsp:include page="../includes/userHeader.jsp"/>
<jsp:include page="../includes/alerts.jsp"/>
<main class="container" style="padding-bottom:3rem;">
    <h1 class="section-title">Orders</h1>
    <%
        if (orders.isEmpty()) {
    %>
    <div class="empty-state">You have not placed any orders yet.</div>
    <%
        } else {
    %>
    <div class="grid" style="gap:1.5rem;">
        <%
            long now = System.currentTimeMillis();
            for (Order order : orders) {
                long minutesSince = order.getCreatedAt() != null ? (now - order.getCreatedAt().getTime()) / 60000 : Long.MAX_VALUE;
                boolean canCancel = "PLACED".equalsIgnoreCase(order.getStatus()) && minutesSince <= 10;
        %>
        <div class="medicine-card">
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <div>
                    <strong>Order #<%= order.getId() %></strong>
                    <p style="margin:0;color:var(--muted);"><%= order.getStatus() %> · <%= order.getCreatedAt() %></p>
                </div>
                <div class="price">₹<%= String.format("%.2f", order.getNetAmount()) %></div>
                <%
                    if (canCancel) {
                %>
                <form action="<%= request.getContextPath() %>/order/cancel" method="post">
                    <input type="hidden" name="orderId" value="<%= order.getId() %>">
                    <button class="btn btn-outline">Cancel</button>
                </form>
                <%
                    }
                %>
            </div>
            <div class="grid" style="gap:.8rem;">
                <%
                    for (OrderItem item : order.getItems()) {
                %>
                <div style="display:flex;align-items:center;gap:.7rem;">
                    <img src="<%= (item.getMedicine().getImageUrl() != null && !item.getMedicine().getImageUrl().trim().isEmpty()) ? (item.getMedicine().getImageUrl().startsWith("/") ? request.getContextPath() + item.getMedicine().getImageUrl() : item.getMedicine().getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>" alt="<%= item.getMedicine().getName() %>" style="width:64px;height:64px;border-radius:12px;object-fit:cover;">
                    <div>
                        <p style="margin:0;font-weight:600;"><%= item.getMedicine().getName() %></p>
                        <p style="margin:0;color:var(--muted);">Qty: <%= item.getQuantity() %></p>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>
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
