<!DOCTYPE html>
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="template/head.jsp"></jsp:include>
<body id="skin-blur-violate">
	<jsp:include page="template/header.jsp"></jsp:include>

	<div class="clearfix"></div>

	<section id="main" class="p-relative" role="main">


		<jsp:include page="template/sidebar.jsp"></jsp:include>

		<!-- Content -->
		<section id="content" class="container">

			<h4 class="page-title">KITS</h4>
			<div class="block-area" id="buttons">
                 <a href="<spring:url value='/web/kits/novo/p' />">
	                 <button class="btn m-r-5" >Novo kit</button>
                 </a>
             </div>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Preço</th>
                                    <th style="width: 50px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="kits" items="${ kits }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/kits/editar/${kit.id}' />">${ kit.id }</a></td>
	                                    <td><a href="<spring:url value='/web/kits/editar/${kit.id}' />">${ kit.nome }</a></td>
	                                    <td> <fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ kit.valor}" /></td>
	                                    <td><a href="<spring:url value='/web/kits/delete/${ kit.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#servicos-menu").addClass("active");
		$("#admin-menu").addClass("active");
	</script>
	
</body>
</html>
