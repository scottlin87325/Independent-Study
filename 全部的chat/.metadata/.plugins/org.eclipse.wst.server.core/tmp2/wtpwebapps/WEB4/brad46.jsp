<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
	Hello
		<c:catch var="err">
			<sql:setDataSource 
		driver="com.mysql.cj.jdbc.Driver" 
		url="jdbc:mysql://localhost/brad" 
		user="root" 
		password=""
		/>
		
		<%
			//用<!-- -->註解會被執行
			//<sql:update var="n">
			//update cust set tel='04-5678901' where id=2;
			//</sql:update> 
		%>
		
		<sql:update var="n">
			update cust set tel='04-5678901' where id=2;
		</sql:update> 
											
		<sql:update var="n">
			insert into cust (name,tel,birthday) values ('A5','123','1999-01-01')
		</sql:update>
			
	  	<sql:update var="n1">
		delete from cust where id>=5;
		</sql:update>					
		</c:catch>
		${err }<br>
		n = ${n }
		n1 = ${n1 }
	</body>
</html>