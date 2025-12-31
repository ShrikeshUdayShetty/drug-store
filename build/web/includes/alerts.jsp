<%
    String logoutMessage = (String) session.getAttribute("logoutMessage");
    if (logoutMessage != null) {
        session.removeAttribute("logoutMessage");
%>
<div class="alert success"><%= logoutMessage %></div>
<%
    }
%>
<% 
    String success = (String) session.getAttribute("successMessage");
    String error = (String) session.getAttribute("errorMessage");
    if (success != null) {
%>
    <div class="alert alert-success"><%= success %></div>
<%
        session.removeAttribute("successMessage");
    }
    if (error != null) {
%>
    <div class="alert alert-error"><%= error %></div>
<%
        session.removeAttribute("errorMessage");
    }
%>
