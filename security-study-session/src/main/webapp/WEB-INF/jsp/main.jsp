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
         
    	$( "#loginButton" ).click(function() {
    		$.ajax({
                type : "POST",
                url : "http://localhost:8081/auth/login",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                		id : $("#id").val()
                	,	password : $("#password").val()
                }),
                error : function(request,status,error){
                    alert(request.responseText);
                },
                success : function(data){
                	window.location.href="http://localhost:8081/member";
                }
            });
    	});
    	
    	$( "#logoutButton" ).click(function() {
    		$.ajax({
                type : "POST",
                url : "http://localhost:8081/auth/logout",
                error : function(){
                    alert('통신실패!!');
                },
                success : function(data){
                	window.location.href="http://localhost:8081/main";
                }
            });
    	});
 
    });
 
</script>

<body>
    Welcome, Main Page
    
    <c:if test="${loginUser eq null}"> 
	    <p>Login</p>
		    ID <input type="text" id="id" name="id"> <br/>
		    PW <input type="password" id="password" name="password"> <br/>
		    <button type="button" id="loginButton">로그인</button> <br/>
		    ${errormsg}
    </c:if>
    
    <c:if test="${loginUser ne null}"> 
	    ${loginUser.name}
	    <button type="button" id="logoutButton" >로그아웃</button>
    </c:if>
    
</body>
</html>