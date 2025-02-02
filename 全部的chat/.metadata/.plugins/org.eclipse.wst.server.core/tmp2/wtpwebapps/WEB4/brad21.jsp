<%@page import="java.util.TreeSet"%>
<%@page import="java.util.HashSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page errorPage="brad22.jsp" %>
    
    <%
    	String name = request.getParameter("name");
    	if(name == null)name="World";
    %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>Hello, World</div>
	<div>Hello, <% out.print(name); %></div>
	<div>Hello, <% out.print((int)(Math.random()*49+1)); %></div>
	<div>Hello, <%= (int)(Math.random()*49+1) %></div>
	<hr>
	<%
		TreeSet<Integer> lottery = new TreeSet<>();
		while(lottery.size()<6)lottery.add((int)(Math.random()*49+1));
		out.print(lottery);
	
	
	
	%>
</body>
</html>