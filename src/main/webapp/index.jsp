<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Enter</title>
</head>
<body>
<h1><%= "Sign in" %></h1><br/>

<form action="<%=request.getContextPath()%>/CheckServlet" method="post">
    Name:     <input type="text" name="name"><br><br>
    Password: <input type="text" name="password"><br><br>
    <input type="submit" value="Enter">
</form>

<h1><%= "Or sign up" %></h1><br/>
<form action="<%=request.getContextPath()%>/RegisterServlet" method="post">
    Name:           <input type="text" name="name1"><br><br>
    Password:       <input type="text" name="password1"><br><br>
    Password again: <input type="text" name="password2"><br><br>
    <input type="submit" value="Register">
</form>


</body>
</html>