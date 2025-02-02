<%@page import="tw.brad.apis.bike"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String name1 = "Brad";
	pageContext.setAttribute("name", name1);
	
	bike b1 = new bike();
	pageContext.setAttribute("bike", b1);
	b1.upspeed();b1.upspeed();b1.upspeed();b1.downspeed();
	
	String name2 = "Eric";
	request.setAttribute("name", name2);
	
	String name3 = "Andy";
	session.setAttribute("name", name3);
	
	String name4 = "Mark";
	application.setAttribute("name", name4);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		Name: ${name }<br>
		Bike: ${bike }<br>
		PageContext Name: ${pageScope.name }<br>
		Request Name: ${requestScope.name }<br>
		Session Name: ${sessionScope.name }<br><!-- 瀏覽器未關就一直記錄著 -->
		Application Name: ${applicationScope.name }<br>
	</body>
</html>