<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8" import="tw.brad.apis.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<% 
response.setHeader("Access-Control-Allow-Origin", "*"); 
response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); 
response.setHeader("Access-Control-Allow-Headers", "Content-Type"); 
%>   
<c:catch var='err'>
	<sql:query var="result" dataSource="jdbc/brad">
		SELECT * FROM souvenirs WHERE name Like ? OR feature like ?
		<sql:param>%${param.search }%</sql:param>
		<sql:param>%${param.search }%</sql:param>
	</sql:query>
	${BradUtils.souvenirs2JSON(result.rows) }	
</c:catch>