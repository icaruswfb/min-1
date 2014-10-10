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
					<form action="/min/web/report/caixa/data" id="fechamento-data-form" method="post">
						<div class="col-lg-3 m-b-15">
								<p>Data do caixa</p>
								<div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="data" id="data-fechamento" value="<fmt:formatDate value="${ data }" pattern="dd/MM/yyyy"/>"/>
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
												Total: R$<fmt:formatNumber value="${ total.total }" minFractionDigits="2" />
												<c:forEach var="parcela" items="${ total.parcelas }">
													<p class="p-l-10 p-t-10  m-0">Total em ${ parcela.parcelas }x: R$<fmt:formatNumber value="${ parcela.total }" minFractionDigits="2" /></p>
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
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalLancado }" minFractionDigits="2" /></strong>
								</div>
							</div>
						 -->
							<div class="row">
								<div class="col-lg-2 float-left">
									<p class="total">Total pago: </p>
								</div>	
								<div class="col-lg-10">
									<strong class="f-s-20">R$<fmt:formatNumber value="${ totalPago }" minFractionDigits="2" /></strong>
								</div>
							</div>
						</div>
						<div class="col-lg-12 m-b-15">
				                 <a class="btn btn-lg" href="/min/web/report/caixa/download/<fmt:formatDate value="${ data }" pattern="dd/MM/yyyy"/>" target="_blank">Download PDF</a>
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
