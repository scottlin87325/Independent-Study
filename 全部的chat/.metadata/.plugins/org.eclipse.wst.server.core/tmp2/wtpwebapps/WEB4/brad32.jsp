<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="member2" class="tw.brad.apis.Member"></jsp:useBean>
<jsp:setProperty property="id" value="3" name="member2"/>
<jsp:setProperty property="account" value="tony" name="member2"/>
<jsp:setProperty property="name" value="Tony" name="member2"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<form action="brad33.jsp">
			<input name='account'>
			<input name='name'>
			<input type="submit" value='Test'>
		</form>
	</body>
</html>