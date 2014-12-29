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
			<h4 class="page-title">Dados de clientes</h4>
			
			<div class="block-area">
				
				<div class="row m-b-10">
						<div class="col-lg-12">
							<div class="col-lg-6">
								<h3>Clientes cadastrados em:</h3>
								<c:forEach var="porAno" items="${ clientesPorAno}">
									<h3>${ porAno.ano } (Total: ${ porAno.total })</h3>
				                    <table class="table table-hover tile">
				                    	<thead>
				                    		<tr>
				                    			<th>Mês</th>
				                    			<th>Total por mês</th>
				                    		</tr>
				                    	</thead>
				                        <tbody>
											<c:forEach var="porMes" items="${ porAno.clientesPorMes }">
												<tr>
													<td>${porMes.mes + 1 }</td>
													<td>${porMes.quantidade }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:forEach>
							</div>
						</div>
					
				</div>
	<div class="clearfix"></div>
				<div class="row m-b-10">
						<div class="col-lg-12">
							<div class="col-lg-6">
								<h3>Clientes por quantidade de visitas:</h3>
			                    <table class="table table-hover tile">
			                    	<thead>
			                    		<tr>
			                    			<th>Visitas</th>
			                    			<th>Quantidade de clientes</th>
			                    		</tr>
			                    	</thead>
			                        <tbody>
										<c:forEach var="porVisitas" items="${ clientesPorVisitas }">
											<tr>
												<td>
													${ porVisitas.visitas }
												</td>
												<td >
													${porVisitas.clientes }
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					
				</div>
			</div>
			
	<div class="clearfix"></div>
	
		</section>











	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#report-clientes-menu").addClass("active");

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