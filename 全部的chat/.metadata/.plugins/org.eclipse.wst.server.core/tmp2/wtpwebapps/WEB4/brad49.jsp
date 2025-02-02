<%@page import="tw.brad.apis.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
    <c:import url="https://data.moa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx" var="data"></c:import>
<c:catch var="err1">
	<sql:setDataSource 
		driver="com.mysql.cj.jdbc.Driver" 
		url="jdbc:mysql://localhost/brad" 
		user="root" 
		password=""
	/>
</c:catch>
<sql:update>
	delete from souvenirs
</sql:update>
<sql:update>
	alter table souvenirs auto_increment = 1
</sql:update>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<c:set var="allgift" value="${BradUtils.parseGift(data) }"></c:set>
		<c:catch var="err2">	
			<c:forEach items="${allgift }" var="souvenirs">
				<sql:update>
					insert into souvenirs(name,feature,pic) values(?,?,?)
					<sql:param>${souvenirs.name }</sql:param>
					<sql:param>${souvenirs.feature }</sql:param>
					<sql:param>${souvenirs.pic }</sql:param>
				</sql:update>
			</c:forEach>
		</c:catch>
		1. ${err1 }<br>
		2. ${err2 }<br>
	</body>
</html>