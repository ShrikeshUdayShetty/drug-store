<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="auth-wrapper">
    <section class="auth-layout">
        <aside class="auth-intro">
            <span class="brand-badge">SkPharma</span>
            <h2>Join a trusted healthcare marketplace</h2>
            <p>
                Create your SkPharma account to order medicines, manage prescriptions, and track deliveries with
                confidence backed by licensed vendors across India.
            </p>
            <ul class="auth-highlights">
                <li>Secure checkout with encrypted payments</li>
                <li>Verified pharmaceutical partners</li>
                <li>Dedicated care team available 24/7</li>
            </ul>
        </aside>

        <div class="auth-card">
            <div>
                <h1>Create your account</h1>
                <p class="form-lead">Provide your details below to access a personalised buying experience.</p>
            </div>

            <jsp:include page="../includes/alerts.jsp"/>

            <form action="<%= request.getContextPath() %>/user/register" method="post" class="form-grid">
                <div class="form-group">
                    <label for="fullName">Full name</label>
                    <input id="fullName" type="text" name="fullName" placeholder="Shrikesh Shetty" required>
                </div>
                <div class="form-group">
                    <label for="email">Email address</label>
                    <input id="email" type="email" name="email" placeholder="you@example.com" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input id="password" type="password" name="password" placeholder="Create a strong password" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone number</label>
                    <input id="phone" type="text" name="phone" placeholder="+91 98765 43210">
                </div>
                <div class="form-group span-2">
                    <label for="address">Primary address</label>
                    <textarea id="address" name="address" rows="3" placeholder="Street, Area, City, State"></textarea>
                </div>
                <div class="form-group span-2">
                    <button type="submit" class="btn btn-primary">Create account</button>
                </div>
            </form>

            <p class="form-footer">
                Already registered?
                <a href="login.jsp">Sign in</a>
            </p>
        </div>
    </section>
</div>
</body>
</html>
