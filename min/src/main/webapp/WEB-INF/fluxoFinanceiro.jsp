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
			<h4 class="page-title">FLUXO FINANCEIRO</h4>
			
			<div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                      <div class="modal-dialog modal-sm">
                          <div class="modal-content">
                              <div class="modal-header">
                                  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                  <h4 class="modal-title">NOVO LANÇAMENTO FINANCEIRO</h4>
                              </div>
                              <div class="modal-body">
                                <form id="novo-lancamento-form">
					                 <label>Tipo de lançamento</label>
					                 <select class="select custom-select m-b-10" name="tipo">
					                 	<option value="ENTRADA">Entrada</option>
					                 	<option value="SAIDA">Saída</option>
					                 </select>
					                 <label>Valor (R$)</label>
	                                <input type="text" class="form-control input-sm mask-money m-b-10" name="valor"/>
					                 <label>Descrição</label>
	                                <input type="text" class="form-control input-sm m-b-10" name="observacao"/>
								</form>
                              </div>
                              <div class="modal-footer">
			                 <button class="btn " type="button" onclick="Financeiro.adicionarLancamento()" data-dismiss="modal">Salvar</button>
                                  <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                              </div>
                          </div>
                      </div>
                  </div>
                 <div class="block-area">
           			<div class="col-lg-6">
               			<a data-toggle="modal" href="#modalWider" class="btn btn-lg">Novo lançamento</a>
           			</div>
                 </div>
			<div class="block-area">
				<div class="row m-b-10">
					<div class="col-lg-12">
						<form action="/min/web/report/fluxoFinanceiro/data" id="fluxo-data-form" method="post">
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
				<div class="col-lg-6" >
                    <table class="table table-hover tile">
                        <thead>
                            <tr>
                                <th style="width: 105px">Data</th>
                                <th>Descrição</th>
                                <th>Valor</th>
                                <th>Saldo (do período)</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:set var="saldo" value="0" />
                        	<c:set var="multiplicador" value="1" />
                        	<c:forEach var="pagamento" items="${ pagamentos }">
	                             <tr>
	                             	<td>
	                             		<fmt:formatDate value="${ pagamento.data }" pattern="dd/MM/yyyy HH:mm"/>
	                             	</td>
	                             	 <td>
	                                 	 ${ pagamento.comanda.cliente.nome }
	                                 	 ${ pagamento.observacao }
	                                 </td>
	                                 <td class="valor">
	                                 	<c:choose>
	                                 		<c:when test="${ pagamento.fluxoPagamento eq 'SAIDA' }">
                        						<c:set var="multiplicador" value="-1" />
	                                 			<i class="debito">
	              	                   				-<fmt:formatNumber minFractionDigits="2" value="${ pagamento.valor }" />
		                                 		</i>
	                                 		</c:when>
	                                 		<c:otherwise>
			                                 	+<fmt:formatNumber minFractionDigits="2" value="${ pagamento.valor }" />
	                                 		</c:otherwise>
	                                 	</c:choose>
	                                 </td>
	                                 <td class="valor">
	                             		<c:set var="saldo" value="${ saldo + (pagamento.valor * multiplicador) }" />    	
	                             		<c:choose>
	                             			<c:when test="${ saldo ge 0 }">
	                             				<fmt:formatNumber minFractionDigits="2" value="${ saldo }" />
	                             			</c:when>
	                             			<c:otherwise>
	                                 			<i class="debito">
	              	                   				-<fmt:formatNumber minFractionDigits="2" value="${ saldo }" />
		                                 		</i>
	                             			</c:otherwise>
	                             		</c:choose>
	                                 </td>
	                             </tr>
                        	</c:forEach>
                        </tbody>
                    </table>
				</div>
               	</div>
			

		</section>


	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#financeiro-menu").addClass("active");
		$("#fluxo-menu").addClass("active");

		$(document).ready(function(){
            	$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				$("#data-fim").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#fluxo-data-form").submit();
					}
				});
				$("#data-inicio").on('blur', function(){
					var dataInicio = $("#data-inicio").val();
					var dataFim = $("#data-fim").val();
					if(dataInicio != "" && dataFim != ""){
						$("#fluxo-data-form").submit();
					}
				});
			});
		
	</script>
	
</body>
</html>
