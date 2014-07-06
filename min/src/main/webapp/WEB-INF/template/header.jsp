<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<header id="header" class="media">
		<a href="" id="menu-toggle"></a> <a class="logo pull-left"
			href="index.html">21min</a>

		<div class="media-body">
			<div class="media" id="top-menu">
				<div class="pull-left tm-icon">
					<a data-drawer="messages" class="drawer-toggle" href=""> <i
						class="sa-top-message"></i> <!-- 
                            <i class="n-count animated">0</i>
                             --> <span>Messages</span>
					</a>
				</div>


				<div id="time" class="pull-right">
					<span id="hours"></span> : <span id="min"></span> : <span id="sec"></span>
				</div>

				<div class="media-body">
					<input type="text" class="main-search">
				</div>
			</div>
		</div>
	</header>