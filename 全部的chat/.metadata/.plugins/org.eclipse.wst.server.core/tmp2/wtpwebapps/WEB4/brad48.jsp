<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<c:catch var='err'>
<sql:setDataSource 
		driver="com.mysql.cj.jdbc.Driver" 
		url="jdbc:mysql://localhost/brad" 
		user="root" 
		password=""
		/>
		<sql:update>
			update cust set name=?, tel=? where id = ?
			<sql:param>A3</sql:param>
			<sql:param>24680</sql:param>
			<sql:param>4</sql:param>
		</sql:update>
		
		
		
</c:catch>
${err }
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		
	</body>
</html>