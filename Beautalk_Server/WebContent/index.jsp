<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

<%
		String header = request.getHeader("category");

		if (header != null) {

			if (header.equalsIgnoreCase("user")) {
	%>
	<jsp:forward page="user.jsp" />
	<%
		} else if (header.equalsIgnoreCase("push")) {
	%>
	<jsp:forward page="push.jsp" />
	<%
		} else if (header.equalsIgnoreCase("search")) {
	%>
	<jsp:forward page="search.jsp" />
	<%
		} else if (header.equalsIgnoreCase("reviews")) {
	%>
	<jsp:forward page="reviews.jsp" />
	<%
		}

		}
	%>

</body>
</html>