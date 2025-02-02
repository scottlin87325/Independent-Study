<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/mytags.tld" prefix="brad" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
	I am brad62<hr />
	<brad:hello2 name="BradV2">
		Hello
	</brad:hello2>
	<%= (int)(Math.random()*49+1) %>
	<hr />
	<brad:hello name="Eric"></brad:hello>
	
	
	</body>
</html>