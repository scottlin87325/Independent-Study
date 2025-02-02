<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty member.account }">
	<c:redirect url="login.jsp" />
</c:if>
<c:if test="${!empty param.delid }">
	<sql:update dataSource="jdbc/brad">
		delete from member where id = ?
		<sql:param>${param.delid }</sql:param>
	</sql:update>
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<h1>Brad Big Company</h1><hr>
		Welcom, ${member.name }<br>
		<img alt="no icon" src="data:image/png; base64, ${member.icon }">
		<br><a href="logout.jsp">Logout</a>
		<hr>
		<a href="addMember.jsp">Add Member</a>
		<hr>
		<table border="1" width="100%">
			<tr>
				<th>ID</th>
				<th>Account</th>
				<th>Name</th>
				<th>Delete</th>
				<th>Edit</th>
			</tr>
			<sql:query var="result" dataSource="jdbc/brad">
				select * from member
			</sql:query>
			<script>
				function delAlert(who){
					var isDel = confirm("是否刪除 *"+who+"* ?")
					return isDel;
				}
			</script>
			<c:forEach items="${result.rows }" var="row">
				<tr>
					<td>${row.id }</td>
					<td>${row.account }</td>
					<td>${row.name }</td>
					<td><a href="?delid=${row.id }" onclick="return delAlert('${row.name}')">Delete</a></td>
					<td><a href="editMember.jsp?editid=${row.id }">Edit</a></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>