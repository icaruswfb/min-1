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
				<form action="/min/web/produtos/precos/simular" id="form-pesquisa-produto" method="post">
	                 <div class="col-lg-9">
	                 	<input class="form-control input-lg m-b-10" placeholder="Pesquisar..." onblur="$('#form-pesquisa-produto').submit();" name="pesquisa" value="${ pesquisa }" />
	                 </div>
	                 <div class="col-lg-3">
	                 	<select class="form-control input-lg m-b-10" onchange="$('#form-pesquisa-produto').submit();" name="categoriaProduto">
	                 		<option></option>
	                		<option value="SALAO" <c:if test="${ categoria == 'SALAO' }">selected="selected"</c:if> >Salão</option>
	                		<option value="LOJA" <c:if test="${ categoria == 'LOJA' }">selected="selected"</c:if> >Loja</option>
	                 	</select>
	                 </div>
					<div class="col-lg-3">
	                 	<input class="form-control m-b-10" placeholder="Novo percentual" onblur="$('#form-pesquisa-produto').submit();" name="percentual" value="${ percentual }" />
	                 </div>
	                 <div class="col-lg-3">
	                 	<input type="submit"  class="btn "/>
	                 </div>
	                 <div class="col-lg-12">
	                 	<a href="javascript:if(confirm('Continuar essa operação irá alterar todos os preços dos produtos listados. Deseja continuar?')){
										                 	$('#form-pesquisa-produto').attr('action', '/min/web/produtos/precos/aplicar');
										                 	$('#form-pesquisa-produto').submit();
										                 }" class="btn btn-lg" >Aplicar novos preços</a>
	                 </div>
				</form>
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
                                    <th>Custo</th>
                                    <th>Revenda</th>
                                    <th>Lucro</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="produto" items="${ produtos }">
	                                <tr>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${produto.produto.id}' />">${ produto.produto.id }</a></td>
	                                    <td><a href="<spring:url value='/web/produtos/editar/${produto.produto.id}' />">${ produto.produto.nome }</a></td>
	                                    <td>${ produto.produto.categoria.descricao }</td>
	                                    <td>${ produto.produto.unidade }</td>
	                                    <td>R$<fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ produto.produto.custoUnitario}" /></td>
	                                    <td>
	                                    	R$<fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ produto.produto.precoRevenda}" />
	                                    	<c:if test="${produto.precoNovo ne null }">
		                                    	(<c:choose>
			                                    	<c:when test="${ produto.produto.precoRevenda ge produto.precoNovo}">
			                                    		<span style="color: green">R$<fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ produto.precoNovo}" /></span>
			                                    	</c:when>
			                                    	<c:otherwise>
			                                    		<span style="color: red">R$<fmt:formatNumber currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2" value="${ produto.precoNovo}" /></span>
			                                    	</c:otherwise>
		                                    	</c:choose>)
	                                    	</c:if>
	                                    </td>
	                                    <td>
	                                    	<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ produto.percentual }" />%
	                                    	<c:if test="${ produto.diferenca ne null }">
		                                    	(
		                                    	<c:choose>
			                                    	<c:when test="${ produto.diferenca gt 0}">
			                                    		<span style="color: red">+<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ produto.diferenca }" /></span>
			                                    	</c:when>
			                                    	<c:otherwise>
			                                    		<span style="color: green"><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ produto.diferenca }" /></span>
			                                    	</c:otherwise>
		                                    	</c:choose>%)
	                                    	</c:if>
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
		$("#produtos-precos-menu").addClass("active");
		$("#admin-menu").addClass("active");
	</script>
	
</body>
</html>
