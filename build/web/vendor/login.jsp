<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="auth-wrapper">
    <section class="auth-layout">
        <aside class="auth-intro">
            <span class="brand-badge">SkPharma Vendors</span>
            <h2>Powering compliant pharmaceutical supply</h2>
            <p>
                Log in to manage catalogue updates, monitor orders, and review performance analytics with real-time
                visibility into your business.
            </p>
            <ul class="auth-highlights">
                <li>Manage inventory and pricing securely</li>
                <li>Monitor fulfilment and settlement status</li>
                <li>Priority vendor success assistance</li>
            </ul>
        </aside>

        <div class="auth-card">
            <div>
                <h1>Vendor dashboard access</h1>
                <p class="form-lead">Enter your registered vendor credentials to continue.</p>
            </div>

            <jsp:include page="../includes/alerts.jsp"/>

            <form action="<%= request.getContextPath() %>/vendor/login" method="post" class="form-grid">
                <div class="form-group span-2">
                    <label for="email">Business email</label>
                    <input id="email" type="email" name="email" placeholder="vendor@example.com" required>
                </div>
                <div class="form-group span-2">
                    <label for="password">Password</label>
                    <input id="password" type="password" name="password" placeholder="Enter your password" required>
                </div>
                <div class="form-group span-2">
                    <button type="submit" class="btn btn-primary">Sign in</button>
                </div>
            </form>

            <p class="form-footer">
                Need onboarding support?
                <a href="<%= request.getContextPath() %>/contact">Reach our vendor desk</a>
            </p>
        </div>
    </section>
</div>
</body>
</html>
