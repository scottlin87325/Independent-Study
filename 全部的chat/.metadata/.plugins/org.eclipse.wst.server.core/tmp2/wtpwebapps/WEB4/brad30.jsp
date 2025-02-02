<%@page import="tw.brad.apis.bike"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	String name = request.getParameter("name");
    	bike b1 = (bike)request.getAttribute("bike");
    %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		I am Brad30.
		<hr>
		Name = <%= name %><br>
		Bike speed = <%= b1.getspeed() %>
	</body>
</html>