<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Total Spend</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<jsp:include page="../includes/userHeader.jsp"/>
<jsp:include page="../includes/alerts.jsp"/>
<main class="container" style="padding-bottom:3rem;">
    <h1 class="section-title">Total Amount Spent</h1>
    <div class="medicine-card" style="text-align:center;">
        <p style="margin:0;color:var(--muted);">Across all completed orders</p>
        <p class="price" style="font-size:2.4rem;margin:1rem 0;">₹<%= String.format("%.2f", (Double) (request.getAttribute("totalSpent") != null ? request.getAttribute("totalSpent") : 0.0)) %></p>
    </div>
</main>
</body>
</html>
