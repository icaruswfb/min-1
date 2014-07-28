<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Sidebar -->
<aside id="sidebar">

	<!-- Sidbar Widgets -->
	<div class="side-widgets overflow">
		<!-- Profile Menu -->
		<div class="text-center s-widget m-b-25 dropdown" id="profile-menu">
			<a href="" data-toggle="dropdown"> <img
				class="profile-pic animated" src="<spring:url value='/img/profile-pic.jpg' />" alt="">
			</a>
			<ul class="dropdown-menu profile-menu animated">
				<li><a href="">My Profile</a> <i class="icon left">&#61903;</i><i
					class="icon right">&#61815;</i></li>
				<li><a href="">Messages</a> <i class="icon left">&#61903;</i><i
					class="icon right">&#61815;</i></li>
				<li><a href="">Settings</a> <i class="icon left">&#61903;</i><i
					class="icon right">&#61815;</i></li>
				<li><a href="">Sign Out</a> <i class="icon left">&#61903;</i><i
					class="icon right">&#61815;</i></li>
			</ul>
			<h4 class="m-0">Malinda Hollaway</h4>
			@malinda-h
		</div>

		<!-- Calendar -->
		<div class="s-widget m-b-25">
			<div id="sidebar-calendar"></div>
		</div>

		<!-- Horários -->
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Próximos horários</h2>

			<div class="s-widget-body" id="feed-agenda">
			</div>
		</div>
		<!-- Feeds -->
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Notícias</h2>

			<div class="s-widget-body">
				<div id="news-feed"></div>
			</div>
		</div>

	</div>

	<!-- Side Menu -->
	<ul class="list-unstyled side-menu">
		<li id="home-menu"><a class="sa-side-home" href="<spring:url value='/web/' />">
				<span class="menu-item">Home</span>
		</a></li>
		<li  id="clientes-menu" ><a class="sa-side-form" href="<spring:url value='/web/clientes/' />"> <span
				class="menu-item">Clientes</span>
		</a></li>
		<li id="agenda-menu"><a class="sa-side-calendar" href="<spring:url value='/web/agenda/' />"> <span
				class="menu-item">Agenda</span>
		</a></li>
		<li class="dropdown" id="financeiro-menu">
             <a class="sa-side-chart" href="">
                 <span class="menu-item">Financeiro</span>
             </a>
             <ul class="list-unstyled menu-item">
             	<li><a id="caixa-menu" href="<spring:url value='/web/report/caixa' />">Caixa</a></li>
				<li><a id="comissao-menu" href="<spring:url value='/web/report/comissao' />">Comissão</a></li>
             </ul>
         </li>
         <li class="dropdown" id="admin-menu">
             <a class="sa-side-folder" href="">
                 <span class="menu-item">Admin</span>
             </a>
             <ul class="list-unstyled menu-item">
             	<li><a id="servicos-menu" href="<spring:url value='/web/servicos/' />">Serviços</a></li>
				<li><a id="produtos-menu" href="<spring:url value='/web/produtos/' />">Produtos</a></li>
				<li><a id="estoque-menu" href="<spring:url value='/web/estoque/' />">Projeção de estoque</a></li>
				<li><a id="employee-menu" href="<spring:url value='/web/funcionarios/' />">Funcionários</a></li>
             </ul>
         </li>
		
		
		
	</ul>

</aside>