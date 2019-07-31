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
                url : "http://localhost:8080/login",
                data: {
                		id : $("#id").val()
                	,	password : $("#password").val()
                },
                error : function(error){
                	console.log(error);
                    alert('존재하지 않는 사용자입니다.');
                },
                success : function(data){
                	window.location.href="http://localhost:8080/member";
                }
            });
    	});
    	
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
    Welcome, Main Page
    
    <c:if test="${loginUser eq null}"> 
	    <p>Login</p>
	    <form action="/login" method="POST">
		    ID <input type="text" id="id" name="id"> <br/>
		    PW <input type="password" id="password" name="password"> <br/>
		    <button type="submit">로그인</button> <br/>
		    ${errormsg}
	    </form>
    </c:if>
    
    <c:if test="${loginUser ne null}"> 
	    ${loginUser.name}
	    <button type="button" id="logoutButton" >로그아웃</button>
    </c:if>
    
</body>
</html>