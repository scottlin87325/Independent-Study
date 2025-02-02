<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8" import="tw.brad.apis.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<% 
response.setHeader("Access-Control-Allow-Origin", "*"); 
response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); 
response.setHeader("Access-Control-Allow-Headers", "Content-Type"); 
%>    
<c:if test="${pageContext.request.method == 'POST' }"> 
<c:catch var='err'>
	<sql:update var="result" dataSource="jdbc/brad">
		insert into souvenirs (name,feature,pic) values(?,?,?)
		<sql:param>${param.name }</sql:param>
		<sql:param>${param.feature }</sql:param>
		<sql:param>${param.pic }</sql:param>
	</sql:update>
	${result }	
</c:catch>
</c:if>