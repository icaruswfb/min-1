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

			<h4 class="page-title">CLIENTES</h4>
			<c:if test="${ hasRole['CAIXA'] }">
				<div class="block-area" id="buttons">
	                 <a href="<spring:url value='/web/clientes/novo/p' />">
		                 <button class="btn m-r-5" >Novo cliente</button>
	                 </a>
	             </div>
			</c:if>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Telefones</th>
                                    <th style="width: 50px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="cliente" items="${ clientes }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/clientes/editar/${cliente.id}' />">${ cliente.id }</a></td>
	                                    <td><a href="<spring:url value='/web/clientes/editar/${cliente.id}' />">${ cliente.nome }</a></td>
	                                    <td>${ cliente.telefone }</td>
	                                    <td><a href="<spring:url value='/web/clientes/delete/${ cliente.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#clientes-menu").addClass("active");
	</script>
	
</body>
</html>
