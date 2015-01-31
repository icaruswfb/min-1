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

		<c:set var="canEdit" value="${ hasRole['CAIXA'] }" />
		

		<!-- Content -->
		<section id="content" class="container">

			<h4 class="page-title">CLIENTE</h4>
            <form:form method="post" action="../salvar" commandName="cliente">
            	<form:hidden path="id" id="cliente-id" />
				<div class="block-area" id="buttons">
					<c:if test="${ hasRole['CAIXA'] }">
		                 <button class="btn m-r-5" type="submit">Salvar</button>
					</c:if>
	                 <a href="<spring:url value='/web/clientes/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do cliente e clique em Salvar</p>
                </div>
                <div class="clearfix"></div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="documento" cssClass="form-control m-b-10" placeholder="RG ou CPF" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="telefone" cssClass="form-control m-b-10" placeholder="Telefones" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="aniversarioStr" cssClass="form-control m-b-10 mask-date validate[required,custom[date]]" placeholder="Aniversário (dd/MM)"  readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="email" cssClass="form-control m-b-10" placeholder="E-mail"  readonly="${ not canEdit }"/>
                </div>
                
                <div class="col-lg-6">
                 	<form:input path="endereco" cssClass="form-control m-b-10" placeholder="Endereço" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="cep" cssClass="form-control m-b-10 mask-cep" placeholder="CEP" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="cidade" cssClass="form-control m-b-10" placeholder="Cidade" readonly="${ not canEdit }"/>
                </div>
                <div class="col-lg-12">
                 	<form:textarea path="observacao" cssClass="form-control m-b-10" placeholder="Observações" readonly="${ not canEdit }"/>
                </div>
             </form:form>
              <div class="clearfix"></div>
            <br />
            <c:if test="${ cliente.id ne null }">
            
            
            		<div class="modal fade" id="modalWider" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">NOVO PAGAMENTO</h4>
                                </div>
                                <div class="modal-body">
                                    <form id="novo-pagamento-form">
										<input type="hidden" name="comandaId" />
						                 <label>Forma de pagamento</label>
						                 <select class="select custom-select" name="formaPagamento" onchange="Cliente.mostrarParcelamento()" id="forma-pagamento">
						                 	<c:forEach var="formaPagamento" items="${ formasPagamento }">
							                 	<option value="${ formaPagamento }">${ formaPagamento.nome }</option>
						                 	</c:forEach>
						                 </select>
						                 <div id="parcelamento">
							                 <br />
							                 <br />
							                 <label>Parcelas</label>
			                                <input type="text" class="form-control input-sm mask-number" name="parcelas"/>
						                 </div>
						                 <br />
						                 <br />
						                 <label>Valor</label>
		                                <input type="text" class="form-control input-sm mask-money" name="valor"/>
									</form>
                                </div>
                                <div class="modal-footer">
                                	
					                 <button class="btn " type="button" onclick="Comanda.pagar()">Salvar</button>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal" id="fechar-popup-pagamento">Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a data-toggle="modal" href="#modalWider" style="display: none;" class="btn btn-sm">Modal - Wider</a>
            
            
            
					<div id="comandas" class="">
						
						<div class="modal fade " id="modalComandaKit" tabindex="-1" role="dialog" aria-hidden="true">
		                     <div class="modal-dialog">
		                         <div class="modal-content">
		                             <div class="modal-header">
		                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                                 <h4 class="modal-title">Adicionar kit</h4>
		                             </div>
		                             <div class="modal-body">
										<div class="listview list-container" id="add-kit-msg">
						                </div>
										<p>Preencha os dados do kit a ser adicionado</p>
										<c:if test="${ ! hasRole['ADMIN'] }">
											<p>Depois de lançado, só pode ser excluído por um administrador.</p>
										</c:if>
										<form action="" id="form-kit" method="post">
										
											<input type="hidden" name="clienteId" value="${ cliente.id }"/>
							                 <label>Kit</label>
							                 <select class="tag-select-limited form-control m-b-10" multiple name="kitId" onchange="Lancamento.buscarValorKit()" >
							                 	<option value=""></option>
							                 </select>
							                 <div class="w-100 float-left m-t-10" id="descricao-kit">
							                 </div>
							                 <label class="m-t-10" >Valor</label>
			                                <input type="text" class="form-control input-sm mask-money" name="valor" disabled="disabled" readonly="readonly"/>
			                                
										</form>
		                             </div>
		                             <div class="modal-footer">
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addKit(false)" >Lançar</button>
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addKit(true)" >Lançar e fechar</button>
		                                 <button type="button" class="btn btn-lg" data-dismiss="modal" id="fechar-modal-kit">Fechar</button>
		                             </div>
		                         </div>
		                     </div>
		                 </div>
					
						<div class="modal fade " id="modalComandaServico" tabindex="-1" role="dialog" aria-hidden="true">
		                     <div class="modal-dialog">
		                         <div class="modal-content">
		                             <div class="modal-header">
		                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                                 <h4 class="modal-title">Adicionar serviço</h4>
		                             </div>
		                             <div class="modal-body">
										<div class="listview list-container" id="add-servico-msg">
						                </div>
										<p>Preencha os dados do serviço a ser adicionado</p>
										<c:if test="${ ! hasRole['ADMIN'] }">
											<p>Depois de lançado, um serviço só pode ser excluído por um administrador.</p>
										</c:if>
										<form action="" id="form-servico" method="post">
										
											<input type="hidden" name="clienteId" value="${ cliente.id }"/>
							                 <label>Serviço</label>
							                 <select class="tag-select-limited form-control m-b-10" multiple name="servicoId" onchange="Comanda.buscarValorServico()" >
							                 	<option value=""></option>
							                 </select>
							                 <label class="m-t-10">Funcionário</label>
							                 <select class="form-control input-sm m-b-10" name="funcionarioId" id="forma-pagamento">
							                 	<option value=""></option>
							                 </select>
							                 <label>Assistente (opcional)</label>
							                 <select class="form-control input-sm m-b-10" name="assistenteId" id="forma-pagamento">
							                 	<option value=""></option>
							                 </select>
							                 <label>Valor</label>
			                                <input type="text" class="form-control input-sm mask-money" name="valor" disabled="disabled" readonly="readonly"/>
			                                
										</form>
		                             </div>
		                             <div class="modal-footer">
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addServico(false)" id="fechar-comanda-button">Lançar</button>
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addServico(true)" id="fechar-comanda-button">Lançar e fechar</button>
		                                 <button type="button" class="btn btn-lg" data-dismiss="modal" id="fechar-modal-servico">Fechar</button>
		                             </div>
		                         </div>
		                     </div>
		                 </div>
	                 	
		                 <!-- Modal de produtos -->
		                 <div class="modal fade " id="modalComandaProduto" tabindex="-1" role="dialog" aria-hidden="true">
		                     <div class="modal-dialog">
		                         <div class="modal-content">
		                             <div class="modal-header">
		                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                                 <h4 class="modal-title">Adicionar produto de revenda</h4>
		                             </div>
		                             <div class="modal-body">
										<div class="listview list-container" id="add-produto-msg">
						                </div>
										<p>Preencha os dados do produto a ser adicionado</p>
										<c:if test="${ ! hasRole['ADMIN'] }">
											<p>Depois de lançado, o produto só poderá ser excluído por um administrador.</p>
										</c:if>
										<form action="" id="form-produto" method="post">
										
											<input type="hidden" name="clienteId" value="${ cliente.id }"/>
							                 <label>Produto</label>
							                 <select class="tag-select-limited form-control m-b-10" multiple name="produtoId" onchange="Comanda.buscarValorProduto()" >
							                 	<option value=""></option>
							                 </select>
							                 <label class="m-t-10">Vendedor</label>
							                 <select class="form-control input-sm m-b-10" name="vendedorId">
							                 	<option value=""></option>
							                 </select>
							                 <label class="label-quantidade">Quantidade</label>
			                                <input type="text" class="form-control input-sm m-b-10 mask-number" name="quantidade" onchange="Comanda.buscarValorProduto()"/>			                                
							                 <label>Valor</label>
			                                <input type="text" class="form-control input-sm mask-money" name="valor" disabled="disabled" readonly="readonly"/>
			                                
										</form>
		                             </div>
		                             <div class="modal-footer">
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addProduto(false)" >Lançar</button>
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addProduto(true)" >Lançar e fechar</button>
		                                 <button type="button" class="btn btn-lg" data-dismiss="modal" id="fechar-modal-produto">Fechar</button>
		                             </div>
		                         </div>
		                     </div>
		                 </div>
		                  <!-- Modal de produtos de serviços-->
		                 <div class="modal fade " id="modalComandaProdutoServico" tabindex="-1" role="dialog" aria-hidden="true">
		                     <div class="modal-dialog">
		                         <div class="modal-content">
		                             <div class="modal-header">
		                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                                 <h4 class="modal-title">Adicionar produto de serviço</h4>
		                             </div>
		                             <div class="modal-body">
										<div class="listview list-container" id="add-produto-servico-msg">
						                </div>
										<p>Preencha os dados do produto a ser adicionado</p>
										<c:if test="${ ! hasRole['ADMIN'] }">
											<p>Depois de lançado, o produto só poderá ser excluído por um administrador.</p>
										</c:if>
										<form action="" id="form-produto-servico" method="post">
										
											<input type="hidden" name="clienteId" value="${ cliente.id }"/>
							                 <label>Produto</label>
							                 <select class="tag-select-limited form-control m-b-10" multiple name="produtoId" onchange="Comanda.buscarValorProduto('servico')" >
							                 	<option value=""></option>
							                 </select>
							                 <label class="label-quantidade m-t-10">Quantidade</label>
			                                <input type="text" class="form-control input-sm m-b-10 mask-number" name="quantidade" onchange="Comanda.buscarValorProduto('servico')"/>			                                
							                 <label>Valor</label>
			                                <input type="text" class="form-control input-sm mask-money" name="valor" disabled="disabled" readonly="readonly"/>
			                                
										</form>
		                             </div>
		                             <div class="modal-footer">
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addProdutoServico(false)" >Lançar</button>
		                                 <button type="button" class="btn btn-lg" onclick="Lancamento.addProdutoServico(true)" >Lançar e fechar</button>
		                                 <button type="button" class="btn btn-lg" data-dismiss="modal" id="fechar-modal-produto-servico">Fechar</button>
		                             </div>
		                         </div>
		                     </div>
		                 </div>
						<form action="" method="post" id="comanda-form">
							
						</form>
						<h4 class='page-title'><a href='javascript:Comanda.expandirComandasAntigas()'>COMANDAS ANTIGAS <span id='comandas-fechadas-acao'>[+]</span></a></h4>
						<div id='comandas-fechadas' class='block-area'></div>
					</div>
             	 <div class="clearfix"></div>
             	 <!-- Modal erro de comanda desatualizada -->
             	 <div class="modal fade" id="modalComandaErro" tabindex="-1" role="dialog" aria-hidden="true">
                     <div class="modal-dialog modal-lg">
                         <div class="modal-content">
                             <div class="modal-header">
                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                 <h4 class="modal-title">AVISO</h4>
                             </div>
                             <div class="modal-body">
								<p>Foi dectado que você está editando uma comanda desatualizada.</p>
								<p>Para evitar perda de dados, atualize os dados da comanda.</p>
                             </div>
                             <div class="modal-footer">
                                 <button type="button" class="btn btn-sm" data-dismiss="modal" onclick="Comanda.findComandaAberta()" id="fechar-comanda-button">Atualizar a comanda</button>
                                 <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                             </div>
                         </div>
                     </div>
                 </div>
                 <a class="btn btn-lg btn-alt m-r-5" data-toggle="modal" href="#modalComandaErro" id="linkModalComandaErro" style="display: none" ></a>
                 
             	 <!-- Modal de fechamento de comanda -->
                   <div class="modal fade" id="modalFechamento" tabindex="-1" role="dialog" aria-hidden="true">
                     <div class="modal-dialog modal-lg">
                         <div class="modal-content">
                             <div class="modal-header">
                                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                 <h4 class="modal-title">Fechamento de comanda de ${ cliente.nome }</h4>
                             </div>
                             <div class="modal-body">
								<div class="listview list-container" id="fechamento-comanda-observacoes">
				                </div>
                             </div>
                             <div class="modal-footer">
                                 <button type="button" class="btn btn-sm" data-dismiss="modal" onclick="Comanda.fecharComanda()" id="fechar-comanda-button">Fechar comanda</button>
                                 <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                             </div>
                         </div>
                     </div>
                 </div>
					<h4 class="page-title m-t-10" id="historico-title"><a href="javascript:Cliente.exibirHistorico()" >HISTÓRICO <span id="historico-title-action">[+]</span></a></h4>
					<div id="historico-block" style="display: none;">
						<div class="block-area">
							<div class="row">
									<!-- Main Chart -->
		                        <div class="col-sm-6" >
									<div class="tile m-b-20 w-100-p" id="dados-compilados">
			                            <h2 class="tile-title">DADOS COMPILADOS</h2>
			                        </div>
		                        </div>
		                        <div class="col-sm-6" >
									<div class="tile">
										<h2 class="tile-title">Visitas x Mês</h2>
										<div class="p-10">
											<div id="grafico-frequencia" class="main-chart" style="height: 200px"></div>
			
										</div>
									</div>
								</div>
								<div class="col-sm-6">
			                        <div class="tile">
		                                <h2 class="tile-title">Servicos</h2>
		                                <div class="p-10">
		                                    <div id="servicos-pizza" class="main-chart" style="height: 200px"></div>
		                                </div>
		                            </div>
		                        </div>
		                        <div class="col-sm-6">
			                        <div class="tile">
		                                <h2 class="tile-title">Atendimentos</h2>
		                                <div class="p-10">
		                                    <div id="funcionarios-pizza" class="main-chart" style="height: 200px"></div>
		                                </div>
		                            </div>
		                        </div>
         				         <c:if test="${ hasRole['ADMIN'] }">
		                        <div class="col-sm-6">
									<a data-toggle="modal" href="#modalLog" onclick="Cliente.findHistorico();" class="btn btn-lg m-b-20">Ver histórico detalhado de atividades cliente</a>
			                         <div class="modal fade" id="modalLog" tabindex="-1" role="dialog" aria-hidden="true">
				                        <div class="modal-dialog modal-lg">
				                            <div class="modal-content">
				                                <div class="modal-header">
				                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				                                    <h4 class="modal-title">Histórico detalhado de ${ cliente.nome }</h4>
				                                </div>
				                                <div class="modal-body">
													<div class="listview list-container" id="historico-list">
									                </div>
				                                </div>
				                                <div class="modal-footer">
				                                    <button type="button" class="btn btn-sm" data-dismiss="modal">Fechar</button>
				                                </div>
				                            </div>
				                        </div>
				                    </div>
	                       		</div>
		               		 </c:if>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
						
				<h4 class="page-title m-t-10">AGENDA</h4>
				<div id="agenda-block">
		            <div class="block-area">
						<div class="row">
							<div class="col-md-12" id="block-calendar">
								<div class="row">
									<div id="calendar" class="p-relative p-10 m-b-20">
										<!-- Calendar Views -->
										<ul class="calendar-actions list-inline clearfix">
											<li class="p-r-0"><a data-view="month" href="#"
												class="tooltips" title="Month"> <i class="sa-list-month"></i>
											</a></li>
											<li class="p-r-0"><a data-view="agendaWeek" href="#"
												class="tooltips" title="Week"> <i class="sa-list-week"></i>
											</a></li>
											<li class="p-r-0"><a data-view="agendaDay" href="#"
												class="tooltips" title="Day"> <i class="sa-list-day"></i>
											</a></li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
	            <div class="clearfix"></div>
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#clientes-menu").addClass("active");

		
		$(document).ready(function(){
             $('.mask-date').mask('00/00');
             $('.mask-cep').mask('00000-000');
             $('.mask-number').mask('#0');
             $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
            <c:if test="${ cliente.id ne null }">
	     		Comanda.init();

				Comanda.hasRoleCaixa = ${ hasRole['CAIXA']} ;
				Comanda.hasRoleOperacional = ${ hasRole['OPERACIONAL'] };
				Comanda.hasRoleAdmin = ${ hasRole['ADMIN'] };
				
             	<c:forEach var="formaPagamento" items="${ formasPagamento }">
             		<c:if test="${formaPagamento.parcelavel}">
             		
             			Cliente.pagamentosParcelados.push('${formaPagamento}');
             		</c:if>
             	</c:forEach>
             	Cliente.mostrarParcelamento();
				Cliente.abrirAgendaCliente();
            </c:if>
		});
	</script>
	<script type="text/javascript">
			$(document).ready(
					function() {
						var date = new Date();
						var d = date.getDate();
						var m = date.getMonth();
						var y = date.getFullYear();
						$('#calendar').fullCalendar(
								{
									minTime: "08:00:00",
									maxTime: "20:00:00",
									lang: 'pt-br',
									header : {
										center : 'title',
										left : 'prev, next',
										right : ''
									},
									firstDay: 0,
									selectable : true,
									selectHelper : true,
									editable : false,
									events : [  ],
									allDaySlot: false,
									slotEventOverlap: false
								});

						var overflowRegular, overflowInvisible = false;
						overflowRegular = $('.overflow').niceScroll();
					});
	
			//Calendar views
			$('body').on('click', '.calendar-actions > li > a', function(e) {
				e.preventDefault();
				var dataView = $(this).attr('data-view');
				$('#calendar').fullCalendar('changeView', dataView);
				//Custom scrollbar
				var overflowRegular, overflowInvisible = false;
				overflowRegular = $('.overflow').niceScroll();
			});
		</script>
</body>
</html>
