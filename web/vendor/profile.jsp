<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.drugstore.model.Vendor" %>
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
    <title>Vendor Profile</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    <style>
        .profile-card {
            background: #fff;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 18px 36px rgba(31, 47, 70, 0.08);
            display: grid;
            gap: 1.5rem;
        }
        .profile-row {
            display: grid;
            grid-template-columns: 160px 1fr;
            gap: 1rem;
            align-items: center;
        }
        .profile-label {
            font-size: 0.85rem;
            text-transform: uppercase;
            letter-spacing: 0.04em;
            color: var(--muted);
            font-weight: 600;
        }
        .profile-value {
            font-size: 1.05rem;
            color: var(--text-strong,#1f2f46);
            font-weight: 500;
        }
        .profile-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 1.5rem;
        }
        .profile-header h1 {
            margin: 0;
        }
        .profile-meta {
            display: flex;
            gap: 1rem;
            align-items: center;
            flex-wrap: wrap;
            color: var(--muted);
        }
        .badge {
            background: rgba(34, 197, 94, 0.12);
            color: #15803d;
            font-weight: 600;
            padding: 0.35rem 0.75rem;
            border-radius: 999px;
            font-size: 0.8rem;
        }
    </style>
</head>
<body>
    <jsp:include page="../includes/vendorHeader.jsp"/>

    <main class="container" style="padding-bottom:3rem;max-width:960px;">
        <div class="profile-card">
            <div class="profile-header">
                <div>
                    <p class="badge">Active Vendor</p>
                    <h1 class="section-title" style="margin-top:0.75rem;">Profile Overview</h1>
                    <p class="muted" style="margin:0;">Keep your store details up to date so customers can trust your brand.</p>
                </div>
                <a href="#" class="btn btn-outline" style="pointer-events:none;opacity:0.6;">Edit (coming soon)</a>
            </div>

            <div class="profile-row">
                <span class="profile-label">Store Name</span>
                <span class="profile-value"><%= vendor.getStoreName() %></span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Contact Person</span>
                <span class="profile-value"><%= vendor.getContactPerson() %></span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Email Address</span>
                <span class="profile-value"><%= vendor.getEmail() %></span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Phone Number</span>
                <span class="profile-value"><%= vendor.getPhone() != null ? vendor.getPhone() : "—" %></span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Member Since</span>
                <span class="profile-value">
                    <%= vendor.getCreatedAt() != null ? vendor.getCreatedAt() : "—" %>
                </span>
            </div>
        </div>
    </main>
</body>
</html>
