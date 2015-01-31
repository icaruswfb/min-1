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

			<h4 class="page-title">PESQUISA</h4>
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
                            	<c:forEach var="pessoa" items="${ pessoas }">
	                                <tr>
	                                	 <td>
	                                    	<c:choose>
	                                    		<c:when test="${ pessoa.funcionario }">
			                                    	<a href="<spring:url value='/web/funcionarios/editar/${pessoa.id}' />">${ pessoa.id }</a>
	                                    		</c:when>
	                                    		<c:otherwise>
			                                    	<a href="<spring:url value='/web/clientes/editar/${pessoa.id}' />">${ pessoa.id }</a>
	                                    		</c:otherwise>
	                                    	</c:choose>
	                                    </td>
	                                    <td>
	                                    	<c:choose>
	                                    		<c:when test="${ pessoa.funcionario }">
			                                    	<a href="<spring:url value='/web/funcionarios/editar/${pessoa.id}' />">${ pessoa.nome } (${ pessoa.funcaoPrincipal })</a>
	                                    		</c:when>
	                                    		<c:otherwise>
			                                    	<a href="<spring:url value='/web/clientes/editar/${pessoa.id}' />">${ pessoa.nome }</a>
	                                    		</c:otherwise>
	                                    	</c:choose>
	                                    </td>
	                                    <td>${ pessoa.telefone }</td>
	                                    <td>
	                                    	<c:choose>
	                                    		<c:when test="${ pessoa.funcionario }">
	                                    			<a href="<spring:url value='/web/funcionarios/delete/${ pessoa.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a>
	                                    		</c:when>
	                                    		<c:otherwise>
	                                    			<a href="<spring:url value='/web/clientes/delete/${ pessoa.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a>
	                                    		</c:otherwise>
	                                    	</c:choose>
	                                    </td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#employee-menu").addClass("active");
	</script>
	
</body>
</html>
