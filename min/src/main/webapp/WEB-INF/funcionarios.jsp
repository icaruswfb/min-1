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

			<h4 class="page-title">FUNCIONÁRIOS</h4>
			<div class="block-area" id="buttons">
                 <a href="<spring:url value='/web/funcionarios/novo/p' />" class="btn m-r-5" >Novo funcionário</a>
             </div>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                    <div class="table-responsive overflow">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 50px">Cor</th>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Documento</th>
                                    <th>Função Principal</th>
                                    <th>Telefones</th>
                                    <th>Aniversário</th>
                                    <th>E-mail</th>
                                    <th style="width: 50px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="funcionario" items="${ funcionarios }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/funcionarios/editar/${funcionario.id}' />"><div class="cor-tabela" style="background-color: ${funcionario.cor};"></div></a></td>
	                                    <td><a href="<spring:url value='/web/funcionarios/editar/${funcionario.id}' />">${ funcionario.id }</a></td>
	                                    <td><a href="<spring:url value='/web/funcionarios/editar/${funcionario.id}' />">${ funcionario.nome }</a></td>
	                                    <td><a href="<spring:url value='/web/funcionarios/editar/${funcionario.id}' />">${ funcionario.documento }</a></td>
	                                    <td>${ funcionario.funcaoPrincipal }</td>
	                                    <td>${ funcionario.telefone }</td>
	                                    <td><fmt:formatDate value="${ funcionario.aniversario }" pattern="dd/MM" /></td>
	                                    <td>${ funcionario.email }</td>
	                                    <td><a href="<spring:url value='/web/funcionarios/delete/${ funcionario.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#employee-menu").addClass("active");
		$("#admin-menu").addClass("active");
	</script>
	
</body>
</html>
