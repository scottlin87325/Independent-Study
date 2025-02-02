<%@page import="java.util.Base64"%>
<%@page import="java.util.SortedMap"%>
<%@page import="jakarta.servlet.jsp.jstl.sql.Result"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="tw.brad.apis.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty param.account }">
	<c:redirect url="login.jsp" />
</c:if>
<sql:query var="rs" dataSource="jdbc/brad">
	select * from member where account = ?
	<sql:param>${param.account }</sql:param>
</sql:query>
<c:if test="${rs.rowCount == 0 }">
	<c:redirect url="login.jsp" />
</c:if>
<c:choose>
	<c:when test="${BCrypt.checkpw(param.passwd, rs.rows[0].passwd) }">
		<%
			Result result = (Result)pageContext.getAttribute("rs");
			SortedMap[] datas = result.getRows();
			SortedMap data = datas[0];
			
			Member member = new Member();
			member.setId((long)data.get("id"));
			member.setAccount((String)data.get("account"));
			member.setPassword((String)data.get("passwd"));
			member.setName((String)data.get("name"));
			try{
				byte[] icon = (byte[])data.get("icon");
				String iconBase64 = Base64.getEncoder().encodeToString(icon);
				
				member.setIcon(iconBase64);
			}catch (Exception e){
				member.setIcon("");
			}
			session.setAttribute("member", member);
		%>
		<c:redirect url="main.jsp" />
	</c:when>
	<c:otherwise>
		<c:redirect url="login.jsp" />
	</c:otherwise>
</c:choose>