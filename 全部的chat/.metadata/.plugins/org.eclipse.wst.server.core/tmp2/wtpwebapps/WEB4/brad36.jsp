<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	//if(request.getMethod().equals("POST"))response.sendError(403);
    
    	String account = request.getParameter("account");
    	String[] habbits = request.getParameterValues("habit");
    	
    	//pageContext.getRequest();
    	Locale loc = request.getLocale();
    	String lang = loc.getDisplayLanguage();
    %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		I am brad36<hr>
		Method: ${pageContext.request.method }<br>
		Language: ${pageContext.request.locale.displayLanguage }<br>
		Account: ${param.account }<br>
		Habits: <br>
			1. ${paramValues.habit[0] }<br>
			2. ${paramValues.habit[1] }<br>
			3. ${paramValues.habit[2] }<br>
			4. ${paramValues.habit[3] }<br>
			5. ${paramValues.habit[4] }<br>
			6. ${paramValues.habit[5] }<br>
	</body>
</html>