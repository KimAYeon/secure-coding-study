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
                	setCookie("Authorization", data, 0);
                	window.location.href="http://localhost:8080/main";
                }
            });
    	});
 
    });
    
    function setCookie(c_name, value, exp) {
    	var date = new Date();
    	  date.setTime(date.getTime() + exp*24*60*60*1000);
    	  document.cookie = c_name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
    }
    
    function getCookie(c_name) {
    	var value = document.cookie.match('(^|;) ?' + c_name + '=([^;]*)(;|$)');
        return value? value[2] : null;
    }
 
</script>

<body>
    Welcome, Admin Page
    <button type="button" id="logoutButton" >로그아웃</button> <br/>

	<img src="${loginUser.profile}"/> ${loginUser.name} <br/>
    
   	<a href="http://localhost:8080/member">회원  페이지 이동</a>
</body>
</html>