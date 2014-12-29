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
			<h4 class="page-title">Dados de serviços de <fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/> até <fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/></h4>
			
			<div class="block-area">
				<div class="row">
					<form action="/min/web/report/servicos/data" id="fechamento-data-form" method="post">
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
					<p>Dados por serviço</p>
					<c:forEach var="servicoPorCategoria" items="${ servicosPorCategoria }">
						<div class="col-lg-12">
						<h3>${servicoPorCategoria.tipoServico.nome } (Total de execuções: ${ servicoPorCategoria.totalExecucoes })</h3>
			                    <table class="table table-hover tile">
			                    	<thead>
			                    		<tr>
			                    			<th>Serviço</th>
			                    			<th>Quantidade de execuções</th>
			                    			<th>Quantidade de clientes</th>
			                    			<th>Reincidências</th>
			                    			<th>Valor total</th>
			                    		</tr>
			                    	</thead>
			                        <tbody>
										<c:forEach var="total" items="${ servicoPorCategoria.servicos }">
											<tr>
												<td>
													<a href="/min/web/servicos/editar/${total.servico.id}">${ total.servico.nome }</a>
												</td>
												<td>
													${total.quantidade }
												</td>
												<td>
													${total.quantidadeClientesDiferentes }
												</td>
												<td>
													${total.quantidadeClientesRecorrentes }
												</td>
												<td>
													 R$<fmt:formatNumber value="${ total.valorTotal }" minFractionDigits="2" maxFractionDigits="2" />
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
						</div>
					</c:forEach>
					
				</div>
			</div>
			
	<div class="clearfix"></div>
	
		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#report-servicos-menu").addClass("active");

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
	</script>
	
</body>
</html>