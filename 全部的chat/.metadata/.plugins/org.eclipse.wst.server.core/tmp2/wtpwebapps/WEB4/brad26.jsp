<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		Hello, World<hr>
		<jsp:include page="brad27.jsp">
			<jsp:param value="6" name="x"/>
			<jsp:param value="3" name="y"/>
		</jsp:include>
		<hr>
		<jsp:include page="brad28.jsp"></jsp:include>
	</body>
</html>