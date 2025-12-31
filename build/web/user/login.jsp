<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="auth-wrapper">
    <section class="auth-layout">
        <aside class="auth-intro">
            <span class="brand-badge">SkPharma</span>
            <h2>Welcome back to better care</h2>
            <p>
                Access your personalised medicine cabinet, reorder essentials, and check order status in a secure
                environment built for patient-first care.
            </p>
            <ul class="auth-highlights">
                <li>Track prescriptions and deliveries effortlessly</li>
                <li>Dedicated support for urgent queries</li>
                <li>Trusted by thousands of families</li>
            </ul>
        </aside>

        <div class="auth-card">
            <div>
                <h1>Sign in to your account</h1>
                <p class="form-lead">Use your registered email and password to continue.</p>
            </div>

            <jsp:include page="../includes/alerts.jsp"/>

            <form action="<%= request.getContextPath() %>/user/login" method="post" class="form-grid">
                <div class="form-group span-2">
                    <label for="email">Email address</label>
                    <input id="email" type="email" name="email" placeholder="you@example.com" required>
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
                New to SkPharma?
                <a href="register.jsp">Create an account</a>
            </p>
        </div>
    </section>
</div>
</body>
</html>
