<%@page import="tw.brad.apis.bike"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	String name = request.getParameter("name");
    	bike b1 = new bike();
    	b1.upspeed();b1.upspeed();b1.upspeed();
    	request.setAttribute("bike", b1);
    %>
<jsp:forward page="brad30.jsp"></jsp:forward>