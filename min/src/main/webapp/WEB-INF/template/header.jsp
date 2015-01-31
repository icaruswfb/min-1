<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<header id="header" class="media">
		<a href="" id="menu-toggle"></a> <a class="logo pull-left"
			href="index.html"><img src="/min/img/logo.png" /></a>

		<div class="media-body">
			<div class="media" id="top-menu">
				<div class="pull-left tm-icon">
					<a data-drawer="messages" class="drawer-toggle" onclick="Tarefa.exibirTarefasBlocoTopo()"> 
						<i 	class="sa-top-message"></i>
						<!-- 
						 --> 
                        <i class="n-count animated" style="display:none">0</i>
                        <span>Mensagens</span>
					</a>
				</div>


				<div id="time" class="pull-right">
					<span id="hours"></span> : <span id="min"></span> : <span id="sec"></span>
				</div>

				<div class="media-body">
					<form action="<spring:url value='/web/pesquisar' />" method="post">
						<input type="text" class="main-search" name="pesquisa">
					</form>
				</div>
			</div>
		</div>
	</header>
	