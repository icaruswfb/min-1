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

			<h4 class="page-title">PRODUTO</h4>
            <form:form method="post" action="../salvar" commandName="produto" id="produto-form">
            	<form:hidden path="id" id="produto-id" />
				<div class="block-area" id="buttons">
	                 <a class="btn m-r-5" onclick="Produto.submit()">Salvar</a>
	                 <a class="btn btn-alt m-r-5" href="<spring:url value='/web/produtos/' />" >Cancelar</a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do produto e clique em Salvar</p>
                </div>
                <div class="clearfix"></div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-2">
                 	<p>Categoria</p>
                	<form:select path="categoria" cssClass="select custom-select" placeholder="Categoria">
                		<form:option value="SALAO">Salão</form:option>
                		<form:option value="LOJA">Loja</form:option>
                	</form:select>
                </div>
                <div class="col-lg-2">
                 	<p>Custo unitário (R$)</p>
                 	<input name="custoUnitario" id="custoUnitario" class="form-control m-b-10 mask-money" placeholder="Custo unitário" value="<fmt:formatNumber minFractionDigits="2" value="${ produto.custoUnitario }" />" />
                </div>
                <div class="col-lg-2">
                 	<p>Preço revenda (R$)</p>
                 	<input name="precoRevenda" id="precoRevenda" class="form-control m-b-10 mask-money" placeholder="Preço sugerido" value="<fmt:formatNumber minFractionDigits="2" value="${ produto.precoRevenda }" />" />
                </div>
                <div class="col-lg-4">
                 	<p>Quantidade mínima para gerar alertas de estoque</p>
                 	<form:input path="quantidadeAlerta" cssClass="form-control m-b-10 mask-number" placeholder="Quantidade mínima para alerta" />
                </div>
                <div class="col-lg-2">
                 	<p>Unidade de medida</p>
                	<form:select path="unidade" cssClass="select custom-select" placeholder="Unidade">
                		<form:option value="ml">Mililitro (ml)</form:option>
                		<form:option value="un">Unidade (un)</form:option>
                		<form:option value="g">Gramas (g)</form:option>
                	</form:select>
                </div>
                
             </form:form>
              <div class="clearfix"></div>
            <br /><br />
            <c:if test="${ produto.id ne null }">
            
                <div class="col-lg-12">
					<div class="alert alert-${produto.situacaoEstoque.cssClass } alert-icon">
                        ${ produto.situacaoEstoque.texto }
                        <i class="icon">${produto.situacaoEstoque.icon }</i>
                    </div>
                </div>
                
				<h4 class="page-title" id="estoque-title"><a href="javascript:Produto.exibirEstoque()" >ESTOQUE <span id="estoque-title-action">[+]</span></a></h4>
				<div id="estoque-block">
		            <div class="block-area">
						<div class="row">
                			<div class="col-lg-2">
                				<p>Quantidade em estoque (${ produto.unidade })</p>
                 				<input class="form-control m-b-10 mask-number" placeholder="0" value="" id="quantidade-estoque" readonly="readonly" />
                			</div>
                		</div>
                		<div class="row">
                			<div class="col-lg-6">
                    			<a data-toggle="modal" href="#modalWider" class="btn btn-lg">Novo lançamento de estoque</a>
                			</div>
						</div>
					</div>
					
					<div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">NOVO LANÇAMENTO DE ESTOQUE</h4>
                                </div>
                                <div class="modal-body">
                                    <form id="novo-lancamento-form">
										<input type="hidden" name="produtoId" value="${ produto.id }" />
						                 <label>Tipo de lançamento</label>
						                 <select class="select custom-select" name="tipo">
						                 	<option value="ENTRADA">Entrada</option>
						                 	<option value="SAIDA">Saída</option>
						                 </select>
						                 <br />
						                 <br />
						                 <label>Quantidade</label>
		                                <input type="text" class="form-control input-sm mask-numer" name="quantidade"/>
									</form>
                                </div>
                                <div class="modal-footer">
					                 <button class="btn " type="button" onclick="Produto.adicionarLancamento()" data-dismiss="modal">Salvar</button>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
            
					
					
					<div class="block-area" >
						<div class="listview list-container" id="lancamentos-estoque">
		                </div>
					</div>
				</div>
				
	            <div class="clearfix"></div>
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#produtos-menu").addClass("active");
		$("#admin-menu").addClass("active");

		$(document).ready(function(){
             $('.mask-number').mask('########0');
             $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		});
		
	</script>
	
</body>
</html>
