<%@page import="tw.brad.apis.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
    	Member member1 = new Member();
    	member1.setId(1); member1.setAccount("brad");
    	member1.setName("Brad");
    %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<jsp:useBean id="member2" class="tw.brad.apis.Member"></jsp:useBean>
		<jsp:setProperty property="id" value="2" name="member2"/>
		<jsp:setProperty property="account" value="tony" name="member2"/>
		<jsp:setProperty property="name" value="Tony" name="member2"/>
		
		Member1:<%= member1.getId() %>:<%= member1.getAccount() %>:<%= member1.getName() %><hr>
		Member2 :
			<jsp:getProperty property="id" name="member2"/>
			<jsp:getProperty property="account" name="member2"/>
			<jsp:getProperty property="name" name="member2"/>
	</body>
</html>