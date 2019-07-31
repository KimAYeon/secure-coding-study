<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false"%>
         
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<title>Spring Boot Application with JSP</title>
</head>

<body>
	<div layout:fragment="content">
		<h1>Error Page</h1>
		error code : ${code} <br/>
		error msg : ${msg} <br/>
		timestamp : ${timestamp}
	</div>
</body>
</html>