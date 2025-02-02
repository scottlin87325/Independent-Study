<%@tag import="java.util.HashMap"%>
<%@ tag pageEncoding="UTF-8" %>
<%@ tag dynamic-attributes="product"%>

<%

	HashMap<String,String> item = (HashMap<String,String>)jspContext.getAttribute("product");
	out.append("<tr>")
	.append("<td>"+item.get("pname")+"</td>")
	.append("<td>"+item.get("price")+"</td>")
	.append("</tr>");

%>