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
			
			
			<!-- Modal Wider -->	
                    <div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">NOVO HORÁRIO</h4>
                                </div>
                                <div class="modal-body">
                                	<div class="listview list-container" id="add-horario-msg">
						                </div>
                                    <form >
					
										<!-- Novo horário -->
							                 <label>Data</label>
			                                <input  type="text" readonly="readonly" disabled="disabled" class="form-control input-sm"  id="dia-agenda" value="${ dataStr }"/>
			                                <input type="hidden" id="dia-agenda-millis" value="${ dataMillis }"/>
			                                <br />
			                                <div class="m-b-10">
                    							<div class="checkbox-mensagem" id="checkbox-folga" style="margin-top: 0px" onclick="Agenda.showFolga();" ></div>
                    							<input type="hidden" name="folga" value="false" id="folga"/> Folga
			                                </div>
			                                <div class="horario-trabalho">
								                 <label>Cliente</label>
								                 <select data-placeholder="Selecionar cliente..." class="tag-select-limited" multiple="multiple" id="cliente-select" >
								                 	<c:forEach var="cliente" items="${ pessoas }">
								                 		<c:if test="${ cliente.funcionario eq false }">
									                 		<option value="${ cliente.id }">${ cliente.nome } [${cliente.id }]</option>
								                 		</c:if>
								                 	</c:forEach>
							                    </select>
			                                </div>
							                 <label class="m-t-10 ">Funcionário</label>
							                  <select data-placeholder="Selecionar funcionário..." class="select custom-select" id="funcionario-select">
							                 	<c:forEach var="funcionario" items="${ pessoas }">
							                 		<c:if test="${ funcionario.funcionario eq true }">
								                 		<option value="${ funcionario.id }">${ funcionario.nome }</option>
							                 		</c:if>
							                 	</c:forEach>
						                    </select>
						            
						                 <label class="m-t-10">Horário</label>
			                                <input type="text" class="form-control input-sm mask-hhmm" id="horario-inicio-agenda"/>
			                            <div class="horario-trabalho">
							                 <label class="m-t-10">Serviços</label>
							                  <select data-placeholder="Selecionar serviços..." class="tag-select" multiple="multiple" id="servico-select" onchange="Agenda.calcularHorario()">
							                 	<c:forEach var="servico" items="${ servicos }">
							                 		<option value="${ servico.id }">${ servico.nome }</option>
							                 	</c:forEach>
						                    </select>
			                            </div>
						                 <label class="m-t-10">Horário de término</label>
			                                <input type="text" class="form-control input-sm mask-hhmm" id="horario-fim-agenda"/>
			                            
					                    <label class="m-t-10">Observações</label>
					                    <textarea class="form-control overflow" rows="3"  id="observacao"></textarea>
									</form>
                                </div>
                                <div class="modal-footer">
                                	
					                 <button class="btn " type="button" onclick="Agenda.addHorario()" >Salvar</button>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                     <a data-toggle="modal" href="#modalWider" style="display: none;" class="btn btn-sm">Modal - Wider</a>
			
			<!-- Modal de edição -->
			
			<div class="modal fade" id="modalEdit" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-sm">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">EDITAR HORÁRIO</h4>
                            </div>
                            <div class="modal-body">
                            <div class="listview list-container" id="edit-horario-msg">
						                </div>
                                <form >
	
						<!-- Editar horário -->
							<input type="hidden" id="editar-horario-id" />
			                 <label>Data</label>
                               <div class="input-icon datetime-pick date-only">
				                     <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm" name="data" id="editar-data-agenda" value="${ dataStr }"/>
				                     <span class="add-on">
				                         <i class="sa-plus"></i>
				                     </span>
				                 </div>
                               <br />
			                 <label>Cliente:</label>
			                 <span id="editar-cliente"></span>
			                 <div id="editar-servicos"></div>
			                 <label class="m-t-10 ">Funcionário</label>
			                  <select data-placeholder="Selecionar funcionário..." class="select custom-select" id="editar-funcionario-select">
			                 	<c:forEach var="funcionario" items="${ pessoas }">
			                 		<c:if test="${ funcionario.funcionario eq true }">
				                 		<option value="${ funcionario.id }">${ funcionario.nome }</option>
			                 		</c:if>
			                 	</c:forEach>
		                    </select>
		            
		                 <label class="m-t-10">Horário</label>
                               <input type="text" class="form-control input-sm mask-hhmm" id="editar-horario-inicio-agenda"/>
		                 <label class="m-t-10">Horário de término</label>
                               <input type="text" class="form-control input-sm mask-hhmm" id="editar-horario-fim-agenda"/>
                           
	                    <label class="m-t-10">Observações</label>
	                    <textarea class="form-control overflow" rows="3"  id="editar-observacao"></textarea>
					</form>
                            </div>
                            <div class="modal-footer">
                				 <button class="btn " type="button" onclick="Agenda.editarHorario()">Salvar</button>
                                <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                            </div>
                        </div>
                    </div>
                </div>
                 <a data-toggle="modal" href="#modalEdit" style="display: none;" class="btn btn-sm">Modal - Wider</a>

			
			<h4 class="page-title">AGENDA - ${ dataStr }</h4>

			<div class="block-area">
				
				<div class="row">
					<!-- Calendario do dia -->
					<div class="col-md-12 clearfix">

						<div id="calendar" class="p-relative p-10 m-b-20">
						</div>
					</div>

				</div>

				<div class="clearfix"></div>
			</div>


		</section>












	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		 Agenda.criarCalendario();
		$(document).ready(
				function() {
					/* Tag Select */
	                (function(){
	                    /* Limited */
	                    $(".tag-select-limited").chosen({
	                        max_selected_options: 1
	                    });
	                    /* Overflow */
	                    $('.overflow').niceScroll();

	                    $("#cliente_select_chzn input").blur(function(){Agenda.verificarCliente()});	                    
	                })();
	                $(window).load(function(){
							Agenda.selecionarData();
		                });

	                
	        		$(".mask-hhmm").mask("##:##");
	                <c:forEach var="servico" items="${ servicos }">
	                	Agenda.servicos[${servico.id}] = {
								id: ${servico.id},
								nome: '${servico.nome}',
								duracao: ${servico.duracao}
							};
	             	</c:forEach>

				});
	</script>
	
	<script type="text/javascript">
		$("#agenda-menu").addClass("active");
	</script>
	
</body>
</html>
