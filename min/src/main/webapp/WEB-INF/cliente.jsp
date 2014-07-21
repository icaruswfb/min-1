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

			<h4 class="page-title">CLIENTE</h4>
            <form:form method="post" action="../salvar" commandName="cliente">
            	<form:hidden path="id" id="cliente-id" />
				<div class="block-area" id="buttons">
	                 <button class="btn m-r-5" type="submit">Salvar</button>
	                 <a href="<spring:url value='/web/clientes/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do cliente e clique em Salvar</p>
                </div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="documento" cssClass="form-control m-b-10" placeholder="RG ou CPF"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="telefone" cssClass="form-control m-b-10" placeholder="Telefones"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="aniversarioStr" cssClass="form-control m-b-10 mask-date validate[required,custom[date]]" placeholder="Aniversário (dd/MM)"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="email" cssClass="form-control m-b-10" placeholder="E-mail"/>
                </div>
                
                <div class="col-lg-6">
                 	<form:input path="endereco" cssClass="form-control m-b-10" placeholder="Endereço"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="cep" cssClass="form-control m-b-10 mask-cep" placeholder="CEP"/>
                </div>
                <div class="col-lg-3">
                 	<form:input path="cidade" cssClass="form-control m-b-10" placeholder="Cidade"/>
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
						                 <select class="select custom-select" name="formaPagamento">
						                 	<option value="Dinheiro">Dinheiro</option>
						                 	<option value="Visa">Visa</option>
						                 	<option value="VisaElectron">Visa Electron</option>
						                 	<option value="MasterCard">MasterCard</option>
						                 	<option value="Maestro">Maestro</option>
						                 	<option value="Diners">Diners</option>
						                 	<option value="Amex">Amex</option>
						                 	<option value="Cheque">Cheque</option>
						                 	<option value="Crédito">Crédito</option>
						                 </select>
						                 <br />
						                 <br />
						                 <label>Valor</label>
		                                <input type="text" class="form-control input-sm" name="valor"/>
									</form>
                                </div>
                                <div class="modal-footer">
                                	
					                 <button class="btn " type="button" onclick="Comanda.pagar()" data-dismiss="modal">Salvar</button>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal">Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a data-toggle="modal" href="#modalWider" style="display: none;" class="btn btn-sm">Modal - Wider</a>
            
            
            
					<div id="comandas" class="">
						<form action="" method="post" id="comanda-form">
							
						</form>
						<h4 class='page-title'><a href='javascript:Comanda.findComandas()'>COMANDAS ANTIGAS <span id='comandas-fechadas-acao'>[+]</span></a></h4>
						<div id='comandas-fechadas' class='block-area'></div>
					</div>
             	 <div class="clearfix"></div>
           		 <br />
				<h4 class="page-title" id="historico-title"><a href="javascript:Cliente.exibirHistorico()" >HISTÓRICO <span id="historico-title-action">[+]</span></a></h4>
				<div id="historico-block">
		            <div class="block-area">
						<div class="row">
							<div class="col-md-9">
								<!-- Main Chart -->
								<div class="tile">
									<h2 class="tile-title">Visitas x Mês</h2>
									<div class="p-10">
										<div id="grafico-frequencia" class="main-chart" style="height: 100px"></div>
		
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="tile" id="dados-compilados">
		                            <h2 class="tile-title">DADOS COMPILADOS</h2>
		                        </div>
							</div>​
						</div>
						<div class="clearfix"></div>
					</div>
					<div class="block-area" >
						<div class="listview list-container" id="historico-list">
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
            <c:if test="${ cliente.id ne null }">
	     		Comanda.init();
            	Comanda.findComandaAberta();
            </c:if>
		});
	</script>
	
</body>
</html>
