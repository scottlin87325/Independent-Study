<%@page import="tw.brad.apis.MyTest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%
	MyTest obj = new MyTest();
	int lottery = obj.getLottery();
	
	session.setAttribute("obj", obj);
	session.setMaxInactiveInterval(10);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		I am brad57<hr>
		Lottery : <%= lottery %><br>
		Lottery : ${obj.lottery }<hr>
		<a href='brad58.jsp'>NEXT</a> 
	</body>
</html>