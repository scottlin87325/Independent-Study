<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="tw.brad.apis.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	Class.forName("com.mysql.cj.jdbc.Driver");
	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/brad", "root", "");
	
	String account = request.getParameter("account");
	String passwd = BCrypt.hashpw(request.getParameter("passwd"), BCrypt.gensalt()) ;
	String name = request.getParameter("name");
	
	byte[] icon =  (byte[])request.getAttribute("icon");
	String sql = "INSERT INTO member (account,passwd,name,icon) VALUES (?,?,?,?)";
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, account);	
	pstmt.setString(2, passwd);	
	pstmt.setString(3, name);	
	pstmt.setBytes(4, icon);
	
	pstmt.executeUpdate();
	response.sendRedirect("main.jsp");
%>