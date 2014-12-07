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
			<h4 class="page-title">COMISSÕES</h4>
			
			<div class="block-area">
				<div class="row m-b-10">
					<div class="col-lg-12">
						<form action="/min/web/report/comissao/data" id="comissao-data-form" method="post">
							<p>Período de visualização</p>
							<input type="hidden" name="ids" value="" id="ids"/>
							<div class="col-lg-3 m-b-15">
								<div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" 
				                     		class="form-control input-sm" 
				                     		name="dataInicio" id="data-inicio"
				                     		value='<fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/>'/>
				                     		
				                     <span class="add-on">
				                         <i class="sa-plus"></i>
				                     </span>
				                 </div>
			                 </div>
							<div class="col-lg-3 m-b-15">
								<div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" 
				                     			class="form-control input-sm" 
				                     			name="dataFim" id="data-fim"
				                     		value='<fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/>' />
				                     <span class="add-on">
				                         <i class="sa-plus"></i>
				                     </span>
				                 </div>
			                 </div>
						</form>
					</div>
				</div>
			</div>
				<div class="block-area m-b-10" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th>Funcionário</th>
                                    <th>Serviços</th>
                                    <th>Serviços auxiliados</th>
                                    <th>Auxiliar</th>
                                    <th>Vendas</th>
                                    <th>Percentuais de venda</th>
                                    <th>Total</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comissao" items="${ comissoesPorFuncionario }">
	                                <tr>
	                                	 <td>
	                                    	<a href="<spring:url value='/web/clientes/editar/${comissao.funcionario.id}' />">${ comissao.funcionario.nome }</a>
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.totalServico }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.totalServicoAuxiliado }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.totalAuxilar }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.totalVendas }" />
	                                    </td>
	                                    <td>
											<ul>
												<c:forEach var="percentual" items="${ comissao.percentuais }">
													<li>
														<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ percentual }" />%:
														 R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.totalPorPercentual[percentual] }" /> 
													</li>
												</c:forEach>
											</ul>
										</td>
	                                    <td>
	                                    	R$<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${ comissao.total }" />
	                                    </td>
	                                    <td>
	                                    	<a data-toggle="modal" href="#modalWider" onclick="Comissao.exibirComissoesPorFuncionario('${comissao.funcionario.id}')" class="btn btn-sm">Detalhes</a>
	                                    </td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                	</div>
			
					<div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">PAGAMENTO DE COMISSÕES</h4>
                                </div>
                                <div class="modal-body" >
                                	<div class="listview list-container" id="pagamento-comissao"></div>
                                	<div class="listview list-container">
                                		<div class="media">
                                			<div class="comissao-detalhe">
                                				<div class="valor-comissao">
                                					<div class="prefix-money">Total: R$</div>
                                					<div class="mask-money" id="comissao-pagamento-total">
                                						0,00
                                					</div>
                                				</div>
                                			</div>
                                		</div>
                                	</div>
                                </div>
                                <div class="modal-footer">
					                 <a class="btn " type="button" onclick="Comissao.downloadComissoes()" target="_blank">Download</a>
					                 <a class="btn " type="button" onclick="Comissao.downloadComissoes(true)" target="_blank">Download de todas</a>
					                 <button class="btn " type="button" onclick="if(confirm('Tem certeza que deseja efetuar este pagamento?')){Comissao.pagarComissoes()}">Pagar</button>
					                 <button class="btn " type="button" onclick="if(confirm('Tem certeza que deseja efetuar este pagamento?')){Comissao.pagarComissoes(true)}">Pagar todas</button>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal" >Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
            

		</section>


	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#comissao-menu").addClass("active");

		$(document).ready(function(){
				$("#data-fim").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#comissao-data-form").submit();
					}
				});
				$("#data-inicio").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#comissao-data-form").submit();
					}
				});
			});
		
	</script>
	
</body>
</html>
