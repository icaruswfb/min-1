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
			<h4 class="page-title">CAIXA <fmt:formatDate value="${ data }" pattern="dd/MM/yyyy"/></h4>
			
			<div class="block-area">
				<div class="row">
					<div class="col-lg-3 m-b-15">
						<form action="/min/web/report/caixa/data" id="fechamento-data-form" method="post">
							<p>Data do caixa</p>
							<div class="input-icon datetime-pick date-only">
			                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="data" id="data-fechamento"/>
			                     <span class="add-on">
			                         <i class="sa-plus"></i>
			                     </span>
			                 </div>
						</form>
					</div>
				</div>
				
				<div class="row m-b-10">
					<p>Total por forma de pagamento</p>
					<div class="col-lg-12">
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Dinheiro</div>
								<p class="p-10" >R$<fmt:formatNumber value="${ totalDinheiro }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Visa</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalVisa }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Visa Electron</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalVisaElectron }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">MasterCard</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalMasterCard }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Maestro</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalMaestro }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Diners</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalDiners }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Amex</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalAmex }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Cheque</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalCheque }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-2">
							<div class="tile">
								<div class="tile-title">Crédito</div>
								<p class="p-10">R$<fmt:formatNumber value="${ totalCredito }" minFractionDigits="2" /></p>
							</div>
						</div>
						<div class="col-lg-12">
							<div class="row">
								<div class="col-lg-2">
									<p class="total">Total lançado: </p>
								</div>
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado }" minFractionDigits="2" /></strong>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-2">
									<p class="total">Total pago: </p>
								</div>	
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalPago }" minFractionDigits="2" /></strong>
								</div>
							</div>
						</div>
					</div>
					
				</div>
			</div>
			
			<h4 class="page-title">COMANDAS ABERTAS</h4>
				<div class="block-area m-b-10" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">Número</th>
                                    <th>Cliente</th>
                                    <th>Valor</th>
                                    <th>Itens</th>
                                    <th>Abertura</th>
                                    <th ></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comanda" items="${ comandas }">
                            		<c:if test="${ comanda.fechamento eq null }">
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
		                                    </td>
		                                    <td>
                                    			<a class="btn btn-sm" href="<spring:url value='/web/clientes/editar/${comanda.cliente.id}' />">Ir para comanda</a>
		                                    </td>
		                                </tr>
                            		</c:if>
                            	</c:forEach>
                            </tbody>
                        </table>
                	</div>
			
			<h4 class="page-title">COMANDAS FECHADAS</h4>
			 <!-- Table Hover -->
                <div class="block-area" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th style="width: 70px">Número</th>
                                    <th>Cliente</th>
                                    <th>Valor</th>
                                    <th>Itens</th>
                                    <th>Abertura</th>
                                    <th>Fechamento</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comanda" items="${ comandas }">
                            		<c:if test="${ comanda.fechamento ne null }">
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
		                                    </td>
		                                    <td>
		                                    	<fmt:formatDate value="${ comanda.fechamento }" pattern="dd/MM/yyyy HH:mm"/>
		                                    </td>
		                                </tr>
                            		</c:if>
                            	</c:forEach>
                            </tbody>
                        </table>
                </div>

		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#caixa-menu").addClass("active");

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
