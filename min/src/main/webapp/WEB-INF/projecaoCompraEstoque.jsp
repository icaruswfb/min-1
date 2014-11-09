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

			<h4 class="page-title">PROJEÇÃO DE ESTOQUE</h4>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                	<p>Produtos com estoque CRÍTICO</p>
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Categoria</th>
                                    <th>Custo da unidade</th>
                                    <th>Estoque atual</th>
                                    <th>Consumo dos últimos 3 meses</th>
                                    <th>Projeção de compra mensal</th>
                                    <th>Valor (Projeção)</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="estoque" items="${ criticos }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.id }</a></td>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.nome }</a></td>
	                                    <td>${ estoque.produto.categoria.descricao }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario}" /></td>
	                                    <td>
	                                    	<fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.estoqueAtual }" />${ estoque.produto.unidade }
	                                    	<c:if test="${ estoque.estoqueAtualUnidade ne null}">
		                                    	(<fmt:formatNumber minFractionDigits="0" maxFractionDigits="2" value="${ estoque.estoqueAtualUnidade }" />un)
	                                    	</c:if>
	                                    </td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.consumidoPeriodo }" />${ estoque.produto.unidade }</td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.projecaoCompraMensal }" />${ estoque.produto.unidade }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario * estoque.projecaoCompraMensal}" /></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>
                <div class="block-area" id="tableHover">
                	<p>Produtos com estoque ALERTA</p>
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Categoria</th>
                                    <th>Custo da unidade</th>
                                    <th>Estoque atual</th>
                                    <th>Consumo dos últimos 3 meses</th>
                                    <th>Projeção de compra mensal</th>
                                    <th>Valor (Projeção)</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="estoque" items="${ alertas }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.id }</a></td>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.nome }</a></td>
	                                    <td>${ estoque.produto.categoria.descricao }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario}" /></td>
	                                    <td>
	                                    	<fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.estoqueAtual }" />${ estoque.produto.unidade }
	                                    	<c:if test="${ estoque.estoqueAtualUnidade ne null}">
		                                    	(<fmt:formatNumber minFractionDigits="0" maxFractionDigits="2" value="${ estoque.estoqueAtualUnidade }" />un)
	                                    	</c:if>
	                                    </td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.consumidoPeriodo }" />${ estoque.produto.unidade }</td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.projecaoCompraMensal }" />${ estoque.produto.unidade }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario * estoque.projecaoCompraMensal}" /></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>
				<div class="block-area" id="tableHover">
                	<p>Produtos sem criticidade de estoque</p>
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">ID</th>
                                    <th>Nome</th>
                                    <th>Categoria</th>
                                    <th>Custo da unidade</th>
                                    <th>Estoque atual</th>
                                    <th>Consumo dos últimos 3 meses</th>
                                    <th>Projeção de compra mensal</th>
                                    <th>Valor (Projeção)</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="estoque" items="${ boas }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.id }</a></td>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${estoque.produto.id}' />">${ estoque.produto.nome }</a></td>
	                                    <td>${ estoque.produto.categoria.descricao }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario}" /></td>
	                                    <td>
	                                    	<fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.estoqueAtual }" />${ estoque.produto.unidade }
	                                    	<c:if test="${ estoque.estoqueAtualUnidade ne null}">
		                                    	(<fmt:formatNumber minFractionDigits="0" maxFractionDigits="2" value="${ estoque.estoqueAtualUnidade }" />un)
	                                    	</c:if>
	                                    </td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.consumidoPeriodo }" />${ estoque.produto.unidade }</td>
	                                    <td><fmt:formatNumber minFractionDigits="0" maxFractionDigits="0" value="${ estoque.projecaoCompraMensal }" />${ estoque.produto.unidade }</td>
	                                    <td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ estoque.produto.custoUnitario * estoque.projecaoCompraMensal}" /></td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>
		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#estoque-menu").addClass("active");
		$("#admin-menu").addClass("active");
	</script>
	
</body>
</html>
