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

		<!-- Feeds -->
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Notícias</h2>

			<div class="s-widget-body">
				<div id="news-feed"></div>
			</div>
		</div>

		<!-- Projects -->
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Próximos horários</h2>

			<div class="s-widget-body">
				<div class="side-border">
					<div class="media-body">
						<small class="text-muted">14:30 com <a href="">Susy</a></small><br />
						<a class="t-overflow" href="">Maria da Siva dos Santos</a>

					</div>
				</div>
				<div class="side-border">
					<div class="media-body">
						<small class="text-muted">15:30 com <a href="">Rose</a></small><br />
						<a class="t-overflow" href="">Márcia Clara Oliveira</a>

					</div>
				</div>
				<div class="side-border">
					<div class="media-body">
						<small class="text-muted">16:00 com <a href="">Susy</a></small><br />
						<a class="t-overflow" href="">Karia Olaf</a>

					</div>
				</div>
				<div class="side-border">
					<div class="media-body">
						<small class="text-muted">17:30 com <a href="">Whoever</a></small><br />
						<a class="t-overflow" href="">Paula Trás</a>

					</div>
				</div>
				<div class="side-border">
					<div class="media-body">
						<small class="text-muted">18:30 com <a href="">Susy</a></small><br />
						<a class="t-overflow" href="">Maria da Siva dos Santos</a>

					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Side Menu -->
	<ul class="list-unstyled side-menu">
		<li id="home-menu"><a class="sa-side-home" href="/min/web/">
				<span class="menu-item">Home</span>
		</a></li>
		<li  id="clientes-menu" ><a class="sa-side-form" href="/min/web/clientes/"> <span
				class="menu-item">Clientes</span>
		</a></li>
		<li id="employee-menu"><a class="sa-side-folder" href="file-manager.html"> <span
				class="menu-item">Funcionários</span>
		</a></li>
		<li id="agenda-menu"><a class="sa-side-calendar" href="calendar.html"> <span
				class="menu-item">Agenda</span>
		</a></li>
	</ul>

</aside>