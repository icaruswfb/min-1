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

			<h4 class="page-title">KIT (Serviços e produtos combinados)</h4>
            <form:form method="post" action="/min/web/kits/salvar" commandName="kit" id="kit-form">
            	<form:hidden path="id"  id="id"/>
				<div class="block-area" id="buttons">
	                 <a class="btn m-r-5" onclick="Kit.submit()">Salvar</a>
	                 <a href="<spring:url value='/web/kits/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do kit e clique em Salvar</p>
                </div>
                <div class="clearfix"></div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome" id="nome"/>
                </div>
                <%--
                <div class="col-lg-2">
                 	<input name="valor" class="form-control m-b-10 mask-money" id="valor" placeholder="Preço sugerido" value="<fmt:formatNumber minFractionDigits="2" value="${ kit.valor }" />"/>
                </div>
                 --%>
                <div class="col-lg-2">
                 	<p class="m-t-10">Soma total: R$<span id="preco" class="mask-money">0,00</span></p>
                </div>
                
                <div class="clearfix"></div>
                <%--
                <div class="col-lg-12">
                	<a class="btn btn-sm m-b-10" onclick="Kit.addServico()" >+ Adicionar serviço</a>
                </div>
                 --%>
                <div class="w-100 float-left" id="bloco-servicos">
                	<c:if test="${ kit.id ne null }">
                		<c:forEach var="servicoKit" items="${ kit.servicos }" varStatus="index">
							<div class='w-100 float-left' id="div-servico-${index.index }">
								<div class='col-md-9'>
									<select name="servico" data-placeholder="Selecione um serviço..." class="tag-select-limited form-control m-b-10" multiple onchange="Kit.calcularValor()" id="servico-${ index.index }">
										<c:forEach var="servico" items="${ servicos }">
											<c:choose>
												<c:when test="${ servicoKit.id == servico.id }">
													<option value="${ servico.id }" selected="selected">${servico.nome }</option>
												</c:when>
												<c:otherwise>
													<option value="${ servico.id }">${servico.nome }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
								<div class='col-md-2'>
									<input class='mask-money form-control input-sm m-b-10' readonly disabled id='preco-servico-${index.index }'  value="<fmt:formatNumber minFractionDigits="2" value="${ servicoKit.preco }" />"></input>
								</div>
								<div class='col-md-1'>
									<a href='#' onclick='Kit.deleteServico("${index.index}")'> <i class='sa-list-delete'></i></a>
								</div>
							</div>           		
                		</c:forEach>
                	</c:if>
                </div>
                
                <div class="clearfix"></div>
				<div class='w-100 float-left'>
					<div class="col-md-12"><a class="btn btn-sm m-b-10" onclick="Kit.addProduto()" >+ Adicionar produto</a></div>
				</div>
                <div class="w-100 float-left" id="bloco-produto">
                	<c:forEach var="produtoKit" items="${ kit.produtos }" varStatus="produtoIndex">
						<div class='w-100 float-left' id='div-produto-${produtoIndex.index }'>
							<div class='col-md-7'>
								<select name="produto" data-placeholder="Selecione um produto..." class="tag-select-limited form-control m-b-10 servico-${index.index }" multiple onchange="Kit.calcularValor()" id="produto-${produtoIndex.index }"  >
									<c:forEach var="produto" items="${ produtos }">
										<c:choose>
											<c:when test="${ produtoKit.produto.id == produto.id }">
												<option value="${ produto.id }" selected="selected">${produto.nome }(${ produto.unidade })</option>
											</c:when>
											<c:otherwise>
												<option value="${ produto.id }">${produto.nome } (${produto.unidade })</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
							<div class='col-md-2'>
								<input onchange='Kit.calcularValor()' value="${ produtoKit.quantidade }" placeholder='Quantidade' class='mask-number form-control input-sm m-b-10' id='quantidade-produto-${produtoIndex.index }'></input>
							</div>
							<div class='col-md-2'>
								<input class='mask-money form-control input-sm m-b-10' value="<fmt:formatNumber minFractionDigits="2" value="${ produtoKit.quantidade * produtoKit.produto.precoRevenda }" />"  readonly disabled id='preco-produto-${produtoIndex.index }'></input>
							</div>
							<div class='col-md-1'><a href='#' onclick='Kit.deleteProduto("${produtoIndex.index}")'> <i class='sa-list-delete'></i></a></div>
						</div>									
					</c:forEach>
                </div>
             </form:form>
              <div class="clearfix"></div>
            <br /><br />
            
		</section>
		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#kits-menu").addClass("active");
		$("#admin-menu").addClass("active");

		$(document).ready(function(){
			Kit.init();         
			<c:if test="${ kit.id ne null }">
	            $(".tag-select-limited").chosen({
	                max_selected_options: 1
	            });
	            
	            /* Overflow */
	            $('.overflow').niceScroll();
				Kit.calcularValor();
			</c:if>	   
            $('.mask-number').mask('##0');
            $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		});


		
	</script>
	
</body>
</html>
