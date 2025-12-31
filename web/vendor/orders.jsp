<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
<%@ page import="com.drugstore.model.Order" %>
<%@ page import="com.drugstore.model.OrderItem" %>
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
    <title>Vendor Orders</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
    <jsp:include page="../includes/vendorHeader.jsp"/>

    <main class="container" style="padding-bottom:3rem;">
        <h1 class="section-title">Orders</h1>

        <%
            List<Order> orders = (List<Order>) request.getAttribute("orders");
            boolean hasOrders = orders != null && !orders.isEmpty();
        %>

        <%
            if (!hasOrders) {
        %>
        <div class="empty-state" style="text-align:center;background:#fff;border-radius:16px;padding:2.5rem;margin-top:2rem;box-shadow:0 18px 36px rgba(31,47,70,0.08);">
            <img src="<%= request.getContextPath() %>/assets/images/orders-empty.svg" alt="No orders" style="max-width:200px;margin:1.5rem auto;display:block;">
            <h2 style="margin-bottom:0.5rem;">No orders yet</h2>
            <p class="muted" style="margin-bottom:1.5rem;">As soon as customers place orders for your medicines, they will appear here for fulfilment.</p>
            <a href="<%= request.getContextPath() %>/vendor/products" class="btn btn-outline">View Products</a>
        </div>
        <%
            } else {
        %>

        <div style="display:grid;gap:2rem;margin-top:2rem;">
            <%
                for (Order order : orders) {
            %>
            <div class="order-card" style="background:#fff;border-radius:18px;padding:2rem;box-shadow:0 16px 32px rgba(15,23,42,0.1);display:grid;gap:1.5rem;">
                <div style="display:flex;flex-wrap:wrap;justify-content:space-between;align-items:flex-start;gap:1rem;">
                    <div>
                        <p style="margin:0;color:var(--muted);text-transform:uppercase;font-size:0.75rem;letter-spacing:0.08em;">Order</p>
                        <h2 style="margin:0;font-size:1.4rem;">#<%= order.getId() %></h2>
                    </div>
                    <div style="text-align:right;">
                        <span style="display:inline-block;padding:0.35rem 0.75rem;border-radius:999px;background:rgba(59,130,246,0.12);color:#1d4ed8;font-weight:600;font-size:0.8rem;">
                            <%= order.getStatus() %>
                        </span>
                        <p class="muted" style="margin:0.5rem 0 0;font-size:0.85rem;">
                            <%= order.getCreatedAt() != null ? order.getCreatedAt() : "" %>
                        </p>
                    </div>
                </div>

                <div style="display:flex;flex-wrap:wrap;gap:1.5rem;color:var(--muted);font-size:0.95rem;">
                    <span><strong>Customer:</strong> <%= order.getUserName() != null ? order.getUserName() : "Unknown" %></span>
                    <span><strong>Email:</strong> <%= order.getUserEmail() != null ? order.getUserEmail() : "—" %></span>
                </div>

                <div style="border:1px solid rgba(15,23,42,0.08);border-radius:14px;overflow:hidden;">
                    <table style="width:100%;border-collapse:collapse;">
                        <thead style="background:rgba(15,23,42,0.03);color:var(--muted);text-transform:uppercase;font-size:0.75rem;letter-spacing:0.08em;">
                            <tr>
                                <th style="text-align:left;padding:0.85rem 1.25rem;">Medicine</th>
                                <th style="text-align:center;padding:0.85rem 1.25rem;width:100px;">Qty</th>
                                <th style="text-align:right;padding:0.85rem 1.25rem;width:120px;">Unit Price</th>
                                <th style="text-align:right;padding:0.85rem 1.25rem;width:110px;">Discount</th>
                                <th style="text-align:right;padding:0.85rem 1.25rem;width:140px;">Line Total</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            List<OrderItem> items = order.getItems();
                            if (items != null) {
                                for (OrderItem item : items) {
                                    double unitPrice = item.getUnitPrice();
                                    double discount = item.getDiscountPercentage();
                                    double effectivePrice = unitPrice - (unitPrice * discount / 100);
                                    double lineTotal = effectivePrice * item.getQuantity();
                        %>
                            <tr style="border-top:1px solid rgba(15,23,42,0.05);">
                                <td style="padding:1rem 1.25rem;">
                                    <strong style="display:block;color:#1f2f46;"><%= item.getMedicine() != null ? item.getMedicine().getName() : "Medicine" %></strong>
                                </td>
                                <td style="text-align:center;padding:1rem 1.25rem;">x<%= item.getQuantity() %></td>
                                <td style="text-align:right;padding:1rem 1.25rem;">₹<%= String.format("%.2f", unitPrice) %></td>
                                <td style="text-align:right;padding:1rem 1.25rem;"><%= discount > 0 ? String.format("%.0f%%", discount) : "—" %></td>
                                <td style="text-align:right;padding:1rem 1.25rem;">₹<%= String.format("%.2f", lineTotal) %></td>
                            </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>

                <div style="display:flex;flex-wrap:wrap;justify-content:space-between;gap:1.5rem;align-items:center;">
                    <div>
                        <p class="muted" style="margin:0;font-size:0.8rem;text-transform:uppercase;letter-spacing:0.12em;">Net Amount</p>
                        <p style="margin:0;font-size:1.5rem;font-weight:700;color:#1f2f46;">₹<%= String.format("%.2f", order.getNetAmount()) %></p>
                    </div>
                    <div>
                        <%
                            boolean canMarkDelivered = !"DELIVERED".equalsIgnoreCase(order.getStatus()) && !"CANCELLED".equalsIgnoreCase(order.getStatus());
                            if (canMarkDelivered) {
                        %>
                        <form action="<%= request.getContextPath() %>/vendor/orders" method="post" style="display:inline-flex;gap:0.5rem;align-items:center;">
                            <input type="hidden" name="orderId" value="<%= order.getId() %>">
                            <button type="submit" class="btn btn-primary">Mark as Delivered</button>
                        </form>
                        <%
                            } else if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
                        %>
                        <span class="badge" style="background:rgba(34,197,94,0.15);color:#15803d;">Delivered</span>
                        <%
                            }
                        %>
                    </div>
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
