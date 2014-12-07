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

			<h4 class="page-title">PRODUTOS</h4>
			<div class="block-area" id="pesquisa-produtos">
				<form action="/min/web/produtos/pesquisar" id="form-pesquisa-produto" method="post">
	                 <div class="col-lg-9">
	                 	<input class="form-control input-lg m-b-10" placeholder="Pesquisar..." onblur="$('#form-pesquisa-produto').submit();" name="pesquisa" value="${ pesquisa }" />
	                 </div>
	                 <div class="col-lg-3">
	                 	<select class="form-control input-lg m-b-10" onchange="$('#form-pesquisa-produto').submit();" name="categoriaProduto">
	                 		<option></option>
	                		<option value="SALAO" <c:if test="${ categoria == 'SALAO' }">selected="selected"</c:if> >SalÃ£o</option>
	                		<option value="LOJA" <c:if test="${ categoria == 'LOJA' }">selected="selected"</c:if> >Loja</option>
	                 	</select>
	                 </div>
				</form>
             </div>
			<div class="block-area" id="buttons">
                 <a href="<spring:url value='/web/produtos/novo/p' />">
	                 <button class="btn m-r-5" >Novo produto</button>
                 </a>
             </div>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Categoria</th>
                                    <th>Unidade</th>
                                    <th>PreÃ§o de revenda</th>
                                    <th>SituaÃ§Ã£o do estoque</th>
                                    <th style="width: 50px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="produto" items="${ produtos }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${produto.id}' />">${ produto.id }</a></td>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${produto.id}' />">${ produto.nome }</a></td>
	                                    <td>${ produto.categoria.descricao }</td>
	                                    <td>${ produto.unidade }</td>
	                                    <td>R$<fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ produto.precoRevenda}" /></td>
	                                    <td>${ produto.situacaoEstoque.nome }</td>
	                                    <td><a href="<spring:url value='/web/produtos/delete/${ produto.id }' />" onclick="return confirm('Tem certeza que deseja excluir?');"><img src='<spring:url value="/img/icon/delete.png" />' /></a></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#produtos-menu").addClass("active");
		$("#admin-menu").addClass("active");
	</script>
	
</body>
</html>
