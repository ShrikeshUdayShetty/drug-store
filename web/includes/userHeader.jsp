<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String uri = request.getRequestURI();
%>
<link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
<header class="navbar">
    <div class="container nav-content">
        <a class="logo" href="<%= ctx %>/home">SkPharma</a>
        <nav class="nav-links">
            <a href="<%= ctx %>/home" class="<%= uri.endsWith("/home") ? "active" : "" %>">Home</a>
            <a href="<%= ctx %>/user/medicines" class="<%= uri.contains("/medicines") ? "active" : "" %>">Medicines</a>
            <a href="<%= ctx %>/user/orders" class="<%= uri.contains("/orders") ? "active" : "" %>">Orders</a>
            <a href="<%= ctx %>/user/cart" class="<%= uri.contains("/cart") ? "active" : "" %>">Cart</a>
            <a href="<%= ctx %>/user/spend" class="<%= uri.contains("/spend") ? "active" : "" %>">Total Spent</a>
        </nav>
        <form action="<%= ctx %>/logout" method="post">
            <button type="submit" class="btn btn-outline">Logout</button>
        </form>
    </div>
</header>
