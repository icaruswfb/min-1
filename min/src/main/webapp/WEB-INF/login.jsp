<!DOCTYPE html>
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!--[if IE 9 ]><html class="ie9"><![endif]-->
	
<jsp:include page="template/head.jsp"></jsp:include>
    <body id="skin-blur-violate">
        <section id="login">
            <header>
            	<img src="/min/img/logo.png" />
                <c:if test="${ error ne null }">
	                <div class="alert alert-danger alert-dismissable fade in error-alert" >
	                	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	                	<div class="texto">
	                		${ error }
	                	</div>
	                </div>
                </c:if>
                <p>Entre com seu usuário e senha para acessar o sistema de gestão 21min</p>
            </header>
        
            <div class="clearfix"></div>
            
            <!-- Login -->
            <form class="box tile animated active" id="box-login" action="<c:url value='/web/login' />" method="post">
                <h2 class="m-t-0 m-b-15">Login</h2>
                <input type="text" class="login-control m-b-10" placeholder="Nome de usuário" name="j_username">
                <input type="password" class="login-control" placeholder="Senha" name="j_password">
                <div class="m-t-10">
	                <button class="btn btn-sm m-r-5 " type="submit" >Logar</button>
	                
	                <small>
	                    <a class="box-switcher" data-switch="box-reset" href="">Esqueceu a senha?</a>
	                </small>
                </div>
            </form>
            
        </section>                      
        

	<jsp:include page="template/scripts.jsp"></jsp:include>
    </body>
</html>
