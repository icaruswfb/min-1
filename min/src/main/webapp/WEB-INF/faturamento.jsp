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
			<h4 class="page-title">Faturamento de <fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/> até <fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/></h4>
			
			<div class="block-area">
				<div class="row">
					<form action="/min/web/report/faturamento/data" id="fechamento-data-form" method="post">
						<div class="col-lg-3 m-b-15">
								<p>Início do período</p>
								<div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="dataInicio" id="data-inicio" value="<fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/>"/>
				                     <span class="add-on">
				                         <i class="sa-plus"></i>
				                     </span>
				                 </div>
						</div>
						<div class="col-lg-3 m-b-15">
								<p>Fim do período</p>
								<div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="dataFim" id="data-fim" value="<fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/>"/>
				                     <span class="add-on">
				                         <i class="sa-plus"></i>
				                     </span>
				                 </div>
						</div>
					</form>
				</div>
				
				<div class="row m-b-10">
					<p>Total por forma de pagamento</p>
					<div class="col-lg-12">
						<div class="col-lg-6">
		                    <table class="table table-hover tile">
		                        <tbody>
									<c:forEach var="total" items="${ totais }">
										<tr>
											<td>
												${ total.formaPagamento.nome }
											</td>
											<td>
												Total: R$<fmt:formatNumber value="${ total.total }" minFractionDigits="2" maxFractionDigits="2" />
												<c:forEach var="parcela" items="${ total.parcelas }">
													<p class="p-l-10 p-t-10  m-0">Total em ${ parcela.parcelas }x: R$<fmt:formatNumber value="${ parcela.total }" minFractionDigits="2" maxFractionDigits="2" /></p>
												</c:forEach>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-12">
						<!-- 
							<div class="row">
								<div class="col-lg-2">
									<p class="total">Total lançado: </p>
								</div>
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado }" minFractionDigits="2" maxFractionDigits="2" /></strong>
								</div>
							</div>
						 -->
							<div class="row">
								<div class="col-lg-2 float-left">
									<p class="total">Total pago: </p>
								</div>	
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalPago }" minFractionDigits="2" maxFractionDigits="2"/></strong>
								</div>
							</div>
						</div>
						<div class="col-lg-12 m-b-15">
				                 <a class="btn btn-lg" href="/min/web/report/faturamento/download/<fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/>/<fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/>" target="_blank">Download PDF</a>
						</div>
					</div>
					
				</div>
			</div>
			
			 <!-- Table Hover -->
			 
							<div class="row">
								<div class="col-lg-2 float-left">
									<p class="total">Total lançado: </p>
								</div>	
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado }" minFractionDigits="2" maxFractionDigits="2"/></strong>
								</div>
							</div>
				<div class="clearfix"></div>
							<div class="row">
								<div class="col-lg-2 float-left">
									<p class="total">Diferença: </p>
								</div>	
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado - totalPago }" minFractionDigits="2" maxFractionDigits="2" /></strong>
								</div>
							</div>
							
				<div class="clearfix"></div>
					<div class="block-area">
							<div class="row">
								<div class="col-lg-6">
									<p>Serviços</p>
				                    <table class="table table-hover tile">
				                        <tbody>
											<c:forEach var="total" items="${ totaisTipoServico }">
												<tr>
													<td>
														${ total.tipoServico.nome}
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ total.total }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							
					<div class="clearfix"></div>
					<div class="row">
								<div class="col-lg-6">
									<p>Total por funcionário principal de cada serviço (não contabiliza auxiliares)</p>
				                    <table class="table table-hover tile">
				                        <tbody>
											<c:forEach var="total" items="${ totaisFuncionario }">
												<tr>
													<td>
														<a href="/min/web/funcionarios/editar/${ total.id }">${ total.funcionario }</a>
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ total.total }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
				<div class="clearfix"></div>
				<div class="row">
								<div class="col-lg-6">
									<p>Total por funcionário em revenda</p>
				                    <table class="table table-hover tile">
				                        <tbody>
											<c:forEach var="total" items="${ totaisFuncionarioRevenda }">
												<tr>
													<td>
														<a href="/min/web/funcionarios/editar/${ total.id }">${ total.funcionario }</a>
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ total.total }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
				<div class="clearfix"></div>
							<div class="row">
								<div class="col-lg-6">
										<p>Totais</p>
					                    <table class="table table-hover tile">
					                        <tbody>
												<tr>
													<td>
														Serviço
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ totalServicos }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
												<tr>
													<td>
														Revenda
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ totalRevenda }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
												<tr>
													<td>
														Produtos do salão
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ totalProdutos }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
												<tr>
													<td>
														Descontos
													</td>
													<td>
														Total: R$<fmt:formatNumber value="${ totalDescontos }" minFractionDigits="2" maxFractionDigits="2" />
													</td>
												</tr>
											</tbody>
										</table>
									</div>
							</div>
				
	<div class="clearfix"></div>
					</div>
	
	
	
		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#faturamento-menu").addClass("active");

		$(document).ready(function(){
			
				$("#data-inicio").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#fechamento-data-form").submit();
					}
				});

				$("#data-fim").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#fechamento-data-form").submit();
					}
				});
				
			});
		Caixa = {
				download:function(){
					var action = $("#fechamento-data-form").attr("action");
					$("#fechamento-data-form").attr("action", "/min/web/report/caixa/download");
					$("#fechamento-data-form").submit();
					$("#fechamento-data-form").attr("action", action);
				}
		};
	</script>
	
</body>
</html>