<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty member.account }">
	<c:redirect url="login.jsp" />
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
		<script>
			function isAccountExist() {
				$.get("isAccountExist.jsp?account="+ $("#account").val(),
					function(data,status){
						if(status=="success"){
							$("#mesg").html(data.count==0?"":"Account Exist");
						}
					}
				);
			}
		</script>
	</head>
	<body>
	Add Member<hr />
		<form action="AddMember" method="post" enctype="multipart/form-data">
			Account: <input name="account" id="account" onblur="isAccountExist()"/><span id="mesg"></span><br>
			Password: <input type="password" name="passwd"/><br>
			Name: <input name="name"/><br>
			Icon: <input type="file" name="icon"/><br>
			<input type="submit" value="Add"/>
		</form>
	</body>
</html>