<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    Vendor vendor = (Vendor) session.getAttribute("vendor");
    if (vendor == null) {
        response.sendRedirect(request.getContextPath() + "/vendor/login.jsp");
        return;
    }

    int totalProducts = 0;
    int pendingOrders = 0;
    double totalSales = 0.0;

    Object productsAttr = request.getAttribute("totalProducts");
    if (productsAttr instanceof Number) {
        totalProducts = ((Number) productsAttr).intValue();
    }
    Object pendingAttr = request.getAttribute("pendingOrders");
    if (pendingAttr instanceof Number) {
        pendingOrders = ((Number) pendingAttr).intValue();
    }
    Object salesAttr = request.getAttribute("totalSales");
    if (salesAttr instanceof Number) {
        totalSales = ((Number) salesAttr).doubleValue();
    }

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    String formattedSales = currencyFormatter.format(totalSales);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    <style>
        body {
            background: linear-gradient(160deg, #f4f6fb 0%, #ffffff 55%);
        }
        .hero-card {
            background: radial-gradient(circle at top left, rgba(34,197,94,0.08), transparent 60%),
                        radial-gradient(circle at bottom right, rgba(59,130,246,0.1), transparent 55%),
                        #ffffff;
            border-radius: 24px;
            padding: 2.5rem;
            box-shadow: 0 20px 40px rgba(31, 47, 70, 0.12);
            display: grid;
            gap: 1.5rem;
        }
        .hero-card h1 {
            margin: 0;
            font-size: 2rem;
            color: #1f2f46;
        }
        .hero-meta {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            color: var(--muted);
        }
        .hero-actions {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
        }
        .dashboard-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.5rem;
            margin: 2.5rem 0 3rem;
        }
        .stat-card {
            background: #ffffff;
            border-radius: 18px;
            padding: 1.75rem;
            box-shadow: 0 16px 30px rgba(15, 23, 42, 0.08);
            display: grid;
            gap: 0.75rem;
        }
        .stat-card h3 {
            margin: 0;
            font-size: 0.9rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: var(--muted);
        }
        .stat-value {
            font-size: 2.4rem;
            font-weight: 700;
            color: #1f2f46;
        }
        .trend {
            font-size: 0.85rem;
            color: #15803d;
            font-weight: 600;
        }
        .quick-panels {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
        }
        .panel-card {
            background: #ffffff;
            border-radius: 18px;
            padding: 1.75rem;
            box-shadow: 0 14px 28px rgba(31, 47, 70, 0.1);
            display: grid;
            gap: 1rem;
        }
        .panel-card ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: grid;
            gap: 0.75rem;
        }
        .panel-card li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: var(--muted);
        }
        .pill {
            background: rgba(59,130,246,0.12);
            color: #1d4ed8;
            padding: 0.35rem 0.75rem;
            border-radius: 999px;
            font-size: 0.75rem;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <jsp:include page="../includes/vendorHeader.jsp"/>
    
    <main class="container" style="padding-bottom:4rem;display:grid;gap:2.5rem;">
        <section class="hero-card">
            <div>
                <p class="pill" style="width:max-content;">Trusted Partner</p>
                <h1>Welcome back, <%= vendor.getStoreName() %></h1>
                <p class="muted" style="margin:0;max-width:540px;">
                    Track orders, manage inventory, and stay ahead of demand — all from your dashboard hub.
                </p>
            </div>
            <div class="hero-meta">
                <span><strong>Contact:</strong> <%= vendor.getContactPerson() %></span>
                <span><strong>Email:</strong> <%= vendor.getEmail() %></span>
                <span><strong>Phone:</strong> <%= vendor.getPhone() != null ? vendor.getPhone() : "—" %></span>
            </div>
            <div class="hero-actions">
                <a href="<%= request.getContextPath() %>/vendor/products/add" class="btn btn-primary">Add new product</a>
                <a href="<%= request.getContextPath() %>/vendor/orders" class="btn btn-outline">View latest orders</a>
            </div>
        </section>

        <section class="dashboard-stats">
            <article class="stat-card">
                <h3>Total Products</h3>
                <span class="stat-value"><%= totalProducts %></span>
                <span class="muted">Keep your catalogue fresh and accurate.</span>
            </article>
            <article class="stat-card">
                <h3>Pending Orders</h3>
                <span class="stat-value"><%= pendingOrders %></span>
                <span class="muted">Orders awaiting fulfilment.</span>
            </article>
            <article class="stat-card">
                <h3>Total Sales</h3>
                <span class="stat-value"><%= formattedSales %></span>
                <span class="trend" style="color:#15803d;">Total realised revenue</span>
            </article>
        </section>
    </main>
    
    <script src="<%= request.getContextPath() %>/assets/js/app.js"></script>
</body>
</html>
