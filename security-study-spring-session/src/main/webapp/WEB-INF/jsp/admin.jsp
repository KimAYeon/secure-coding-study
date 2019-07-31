<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <title>Spring Boot Application with JSP</title>
</head>

<script type="text/javascript" language="javascript">
 
    $(document).ready(function(){
         
    	$( "#logoutButton" ).click(function() {
    		$.ajax({
                type : "POST",
                url : "http://localhost:8080/auth/logout",
                error : function(){
                    alert('통신실패!!');
                },
                success : function(data){
                	window.location.href="http://localhost:8080/main";
                }
            });
    	});
 
    });
 
</script>

<body>
    Welcome, Admin Page
    
    ${loginUser.name}
    <button type="button" id="logoutButton" >로그아웃</button> <br/>
    
   	<a href="http://localhost:8080/member">회원  페이지 이동</a>
</body>
</html>