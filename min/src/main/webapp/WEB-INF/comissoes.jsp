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
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comissao" items="${ comissoesPorFuncionario }">
	                                <tr>
	                                	 <td>
	                                    	<a href="<spring:url value='/web/clientes/editar/${comissao.funcionario.id}' />">${ comissao.funcionario.nome }</a>
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.totalServico }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.totalServicoAuxiliado }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.totalAuxilar }" />
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.totalVendas }" />
	                                    </td>
	                                    <td>
											<ul>
												<c:forEach var="percentual" items="${ comissao.percentuais }">
													<li>
														<fmt:formatNumber minFractionDigits="2" value="${ percentual }" />%:
														 R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.totalPorPercentual[percentual] }" /> 
													</li>
												</c:forEach>
											</ul>
										</td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" value="${ comissao.total }" />
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
