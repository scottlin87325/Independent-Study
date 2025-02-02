<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<c:set var="x" value="10" />
		<c:set var="x" value="100" scope="request"/>
		<c:set var="y">777</c:set>
		x = ${x }<br><!-- 預設pageScop -->
		x = ${requestScop.x }<br>
		x = ${pageScop.x }<br>
		y = ${y }<br>
		Hello, World<br>
		<c:out value="Hello, World" /><br>
		param.x = ${param.x }<br>
		param.x = <c:out value="${param.x }" default="no x" /><br>
		
		<jsp:useBean id="member" class="tw.brad.apis.Member"></jsp:useBean>
		<c:set target="${member }" property="id">4</c:set>
		<c:set target="${member }" property="account">brad</c:set>
		<c:set target="${member }" property="name">Brad</c:set>
		${member.id }:${member.account }:${member.name }<br>
		${member }<hr>
		<% 
			//<c:remove var="member" />
		%>
		<c:remove var="member" />
		${member }
		
		
		
		
	</body>
</html>