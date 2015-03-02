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
			<h4 class="page-title">COMANDAS</h4>
			
			<div class="block-area">
				<div class="row m-b-10">
					<div class="col-lg-12">
						<form action="/min/web/report/comandas/data" id="comanda-data-form" method="post">
						<!-- 
							<div class="col-lg-12 m-b-15">
        			         	<input name="pesquisa" class="form-control input-lg m-b-10" placeholder="Pesquisa" value="${pesquisa }"/>
							</div>
						 -->
							<div class="col-lg-12">
								<p>Período de visualização</p>
							</div>
							<div class="col-lg-3 m-b-15">								
								<input type="hidden" name="ids" value="" id="ids"/>
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
			<div class="block-area m-b-10">
				<a class="btn btn-lg" data-toggle="modal"  href="#modalWider" target="_blank">Download</a>
				
				<div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">Número do lote</h4>
                                </div>
                                <div class="modal-body">
                                	<input class="form-control input-sm m-b-10 mask-number" name="numeroLote" id="numero-lote" />
                                </div>
                                <div class="modal-footer">
					                 <a class="btn " id="btn-download" href="#" type="button" target="_blank" >Download</a>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal" id="fechar-popup-pagamento">Cancelar</button>
                                </div>
                             </div>
                          </div>
                     </div>
				
				
			</div>
				<div class="block-area m-b-10" id="tableHover">
                        <table class="table table-hover tile">
                            <thead>
                                <tr>
                                    <th>Nr</th>
                                    <th>Data</th>
                                    <th>Cliente</th>
                                    <th>Observação</th>
                                    <th>Valor</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="comanda" items="${ comandas }">
	                                <tr>
	                                	 <td>
	                                    	<a href="<spring:url value='/web/clientes/editar/${comanda.cliente.id}' />">${comanda.id}</a>
	                                    </td>
	                                    <td>
	                                    	<fmt:formatDate value="${ comanda.abertura }" pattern="dd/MM/yyyy"/>
	                                    </td>
	                                    <td>
	                                    	${ comanda.cliente.nome }
	                                    </td>
	                                    <td>
	                                    	${ comanda.numeroNota }
	                                    </td>
	                                    <td>
	                                    	R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ comanda.valorTotal }" />
	                                    </td>
	                                </tr>
                            	</c:forEach>
                            </tbody>
                        </table>
                	</div>
				
				<div class="block-area" >
					<div class="row">
						<div class="col-lg-6">
						<p>Serviços</p>
	                    <table class="table table-hover tile">
	                        <tbody>
	                        	<tr>
	                        		<td>Valor total</td>
	                        		<td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ valorTotal }" /></td>
	                        	</tr>
	                        	<tr>
	                        		<td>Quantidade de comandas</td>
	                        		<td>${ quantidadeComandas }</td>
	                        	</tr>
	                        	<tr>
	                        		<td>Ticket médio</td>
	                        		<td>R$<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ valorTotal / quantidadeComandas }" /></td>
	                        	</tr>
	                        </tbody>
	                      </table>
				                      
						</div>
					</div>
				</div>
                    
            

		</section>


	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#comandas-menu").addClass("active");

		$(document).ready(function(){
           		$('.mask-number').mask('#####0');
				$("#data-fim").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#comanda-data-form").submit();
					}
				});
				$("#data-inicio").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#comanda-data-form").submit();
					}
				});
				$("#numero-lote").on('change', function(){
					if($("#numero-lote").val()){
						var url = '/min/web/report/comandas/download/<fmt:formatDate value="${ dataInicio }" pattern="dd/MM/yyyy"/>/<fmt:formatDate value="${ dataFim }" pattern="dd/MM/yyyy"/>/' + $("#numero-lote").val()
						$("#btn-download").attr("href", url);
					}
				});
			});

	</script>
	
</body>
</html>
