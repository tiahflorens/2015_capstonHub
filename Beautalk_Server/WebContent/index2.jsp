
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="work.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
</head>
<body>
	<%
		String header = request.getHeader("category");
		String div = request.getHeader("division");
		String data = request.getParameter("data");
		String ss = null;

		if (header != null) {

			if (header.equalsIgnoreCase("user")) {

				ss= new User().work(div, data);
			}
		}else if(data != null){
			System.out.println("data is not null");
				ss = new User().work(request.getParameter("division"), data);
		}
	%>
	<jsp:forward page="result.jsp">
		<jsp:param name="result" value="<%=ss%>" />
	</jsp:forward>

</body>
</html>