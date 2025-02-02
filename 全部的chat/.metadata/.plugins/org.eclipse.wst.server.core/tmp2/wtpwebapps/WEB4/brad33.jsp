<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <jsp:useBean id="member2" class="tw.brad.apis.Member"></jsp:useBean>
<jsp:setProperty property="id" value="3" name="member2"/>
<jsp:setProperty property="account" value="${param.account}" name="member2"/>
<jsp:setProperty property="name" value="${param.name }" name="member2"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		Member2 :
			<jsp:getProperty property="id" name="member2"/>
			<jsp:getProperty property="account" name="member2"/>
			<jsp:getProperty property="name" name="member2"/>
		Member2 :
		<%= member2.getId() %>:<%= member2.getAccount() %>:<%= member2.getName() %><hr>
		${member2 }<br>
		${Math.random() }:${Math.PI }<br>
	</body>
</html>