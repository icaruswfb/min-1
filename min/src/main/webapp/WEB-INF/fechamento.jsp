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
			<h4 class="page-title">FECHAMENTO <fmt:formatDate value="${ data }" pattern="dd/MM/yyyy"/></h4>
			
			<div class="block-area">
				<div class="row">
					<div class="col-lg-3 m-b-15">
						<form action="/min/web/report/fechamento/data" id="fechamento-data-form" method="post">
							<p>Data do fechamento</p>
							<div class="input-icon datetime-pick date-only">
			                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="data" id="data-fechamento"/>
			                     <span class="add-on">
			                         <i class="sa-plus"></i>
			                     </span>
			                 </div>
						</form>
					</div>
				</div>
				
				<div class="row">
					<p>Total por forma de pagamento</p>
					<table class="table table-hover tile">
						<thead>
							<tr>
								<th>Dinheiro</th>
								<th>Visa</th>
								<th>Visa Electron</th>
								<th>MasterCard</th>
								<th>Maestro</th>
								<th>Diners</th>
								<th>Amex</th>
								<th>Cheque</th>
								<th>Crédito</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>R$<fmt:formatNumber value="${ totalDinheiro }" minFractionDigits="2" /></<td>
								<td>R$<fmt:formatNumber value="${ totalVisa }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalVisaElectron }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalMasterCard }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalMaestro }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalDiners }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalAmex }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalCheque }" minFractionDigits="2" /></td>
								<td>R$<fmt:formatNumber value="${ totalCrédito }" minFractionDigits="2" /></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-lg-10">
						<p class="total">Total lançado: </p>
					</div>
					<div class="col-lg-2">
						<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado }" minFractionDigits="2" /></strong>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-10">
						<p class="total">Total pago: </p>
					</div>	
					<div class="col-lg-2">
						<strong class="f-s-20">R$<fmt:formatNumber value="${ totalPago }" minFractionDigits="2" /></strong>
					</div>
				</div>
			</div>
			
			<h4 class="page-title">COMANDAS DO DIA</h4>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">Número</th>
                                    <th>Cliente</th>
                                    <th>Valor</th>
                                    <th>Itens</th>
                                    <th>Abertura/Fechamento</th>
                                    <th ></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comanda" items="${ comandas }">
	                                <tr>
	                                	 <td>
	                                    	${ comanda.id }
	                                    </td>
	                                    <td>
	                                    	<a href="<spring:url value='/web/clientes/editar/${comanda.cliente.id}' />">${ comanda.cliente.nome }</a>
	                                    </td>
	                                    <td>R$ <fmt:formatNumber  minFractionDigits="2" value="${ comanda.valorCobrado }"/></td>
	                                    <td>
											<ul>
												<li>
													Serviços
													<ul>
														<c:forEach var="servico" items="${ comanda.servicos }">
															<li>${ servico.servico.nome }</li>
														</c:forEach>
													</ul>
												</li>
												<li>
													Produtos
													<ul>
														<c:forEach var="produto" items="${ comanda.produtos }">
															<li>${ produto.produto.nome }</li>
														</c:forEach>
													</ul>
												</li>
											</ul>
										</td>
	                                    <td>
	                                    	<fmt:formatDate value="${ comanda.abertura }" pattern="dd/MM/yyyy HH:mm"/>
	                                    	<c:if test="${ comanda.fechamento != null }">
	                                    		- <fmt:formatDate value="${ comanda.fechamento }" pattern="dd/MM/yyyy HH:mm"/>
	                                    	</c:if>
	                                    </td>
	                                    <td>
	                                    		<c:if test="${ comanda.fechamento eq null }">
	                                    			<a class="btn btn-sm" onclick="Comanda.fecharComandaPorId('${comanda.id}')">Fechar</a>
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
		$("#report-menu").addClass("active");
		$("#fechamento-menu").addClass("active");

		$(document).ready(function(){
				$("#data-fechamento").on('blur', function(){
					var data = $("#data-fechamento").val();
					if(data != ""){
						$("#fechamento-data-form").submit();
					}
				});
			});
		
	</script>
	
</body>
</html>
