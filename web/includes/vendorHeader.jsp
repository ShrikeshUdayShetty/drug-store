<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
<% 
    String ctx = request.getContextPath();
    String uri = request.getRequestURI();
    Vendor vendor = (Vendor) session.getAttribute("vendor");
%>
<header class="header">
    <div class="container">
        <div class="header-content">
            <a href="<%= ctx %>/vendor/dashboard" class="logo" style="display:flex;align-items:center;gap:0.65rem;text-decoration:none;">
                <img src="<%= ctx %>/images/logo.png" alt="Drugstore" style="width:42px;height:42px;object-fit:contain;">
                <span style="font-weight:700;font-size:1.35rem;color:var(--text-strong,#111827);">Drugstore</span>
            </a>
            
            <nav class="nav-links">
                <a href="<%= ctx %>/vendor/dashboard" class="<%= uri.endsWith("/vendor/dashboard") ? "active" : "" %>" style="display:flex;align-items:center;gap:0.4rem;">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M3 12l9-9 9 9"/><path d="M9 21V9h6v12"/></svg>
                    Dashboard
                </a>
                <a href="<%= ctx %>/vendor/products" class="<%= uri.contains("/products") ? "active" : "" %>" style="display:flex;align-items:center;gap:0.4rem;">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="14" rx="2"/><path d="M3 10h18"/><path d="M7 21h10"/><path d="M9 17h6"/></svg>
                    Products
                </a>
                <a href="<%= ctx %>/vendor/orders" class="<%= uri.contains("/orders") ? "active" : "" %>" style="display:flex;align-items:center;gap:0.4rem;">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M4 5h16l-1.5 9h-13z"/><path d="M6 5l1-2h10l1 2"/><circle cx="9" cy="19" r="1.5"/><circle cx="17" cy="19" r="1.5"/></svg>
                    Orders
                </a>
            </nav>
            
            <div class="user-actions" style="display:flex;align-items:center;gap:1rem;">
                <% if (vendor != null) { %>
                    <div class="vendor-chip" style="display:flex;align-items:center;gap:0.5rem;background:var(--surface, #f4f6fb);padding:0.5rem 1rem;border-radius:999px;">
                        <span style="font-weight:600;color:var(--text-strong,#1e1f24);"><%= vendor.getStoreName() %></span>
                        <span style="font-size:0.85rem;color:var(--muted);">Vendor</span>
                    </div>
                    <a href="<%= ctx %>/vendor/profile" class="btn btn-text" style="padding:0;">Profile</a>
                    <a href="<%= ctx %>/vendor/logout" class="btn btn-outline">Logout</a>
                <% } else { %>
                    <a href="<%= ctx %>/vendor/login.jsp" class="btn btn-outline">Vendor Login</a>
                <% } %>
            </div>
        </div>
    </div>
</header>

<jsp:include page="alerts.jsp"/>
