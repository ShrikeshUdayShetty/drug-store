<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String uri = request.getRequestURI();
%>
<div class="sidebar">
    <h2>Vendor Panel</h2>
    <nav>
        <a href="<%= ctx %>/vendor/dashboard" class="<%= uri.contains("/vendor/dashboard") ? "active" : "" %>">Dashboard</a>
        <a href="<%= ctx %>/vendor/add-product" class="<%= uri.contains("/add-product") ? "active" : "" %>">Add Product</a>
        <a href="<%= ctx %>/vendor/orders" class="<%= uri.contains("/vendor/orders") ? "active" : "" %>">View Orders</a>
    </nav>
    <form action="<%= ctx %>/vendor/logout" method="post" style="margin-top:2rem;">
        <button type="submit" class="btn btn-outline" style="width:100%;">Logout</button>
    </form>
</div>
