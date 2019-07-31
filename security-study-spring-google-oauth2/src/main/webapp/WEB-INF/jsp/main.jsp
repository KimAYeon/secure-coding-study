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
                url : "http://localhost:8080/auth/login",
                //dataType : "text/plain;charset=UTF-8",
                contentType: "application/x-www-form-urlencoded; charset=utf-8", 
                data: $("#loginForm").serialize(),
                error : function(error){
                	console.log(error);
                    alert('존재하지 않는 사용자입니다.');
                },
                success : function(data){        
                	console.log(data);
                	setCookie("Authorization", data, 60);
                	//moveMemberPage(data);
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
    
    function moveMemberPage(data) {
    	$.ajax({
  		  	url: "http://localhost:8080/member",
  		  	type: 'GET',
  		  	contentType: "application/x-www-form-urlencoded; charset=utf-8",
   			crossDomain: true,
  			dataType: 'json',
  			xhrFields: {
  		    	withCredentials: true
  			}
  			//headers: { 'Authorization': getCookie("Authorization") }
  		});
    }
    
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
    Welcome, Main Page
    
    <c:if test="${loginUser eq null}"> 
	    <p>Login</p>
	    <form id="loginForm" action="/auth/login" method="post">
		    ID <input type="text" id="id" name="id"> <br/>
		    PW <input type="password" id="password" name="password"> <br/>
		    <button type="button" id="loginButton">로그인</button> <br/>
		</form>
 		<a href="http://localhost:8080/login/google">구글 로그인</a> 
		    ${errormsg}
    </c:if>
    
    <c:if test="${loginUser ne null}"> 
	    ${loginUser.name}
	    <button type="button" id="logoutButton" >로그아웃</button> <br/>
	    
	    <a href="http://localhost:8080/member">회원  페이지 이동</a>  <br/>
    
   		<a href="http://localhost:8080/admin">관리자 페이지 이동</a>
    </c:if>
    
</body>
</html>