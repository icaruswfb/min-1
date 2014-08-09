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
		
		<c:set var="canEdit" value="false" />
		<c:choose>
			<c:when test="${ hasRole['ADMIN'] }">
				<c:set var="canEdit" value="true" />
			</c:when>
			<c:otherwise>
				<c:if test="${ loggedUser.pessoa.id eq funcionario.id }">
					<c:set var="canEdit" value="true" />
				</c:if>
			</c:otherwise>
		</c:choose>
		<!-- Content -->
		
		<section id="content" class="container">
		<!-- 
			ADMIN = ${ hasRole['ADMIN'] }; OPERACIONAL = ${ hasRole['OPERACIONAL'] }; CAIXA = ${ hasRole['CAIXA'] }; é o logado? ${ loggedUser.pessoa.id eq funcionario.id };
		 -->
			<h4 class="page-title">FUNCIONÁRIO</h4>
	            	
	           	<div class="block-area">
	           		<div class="row">
	           			<div class="col-md-3">
							<form id="upload">
								<div class="block-area m-b-10">
				            	 <p>Imagem do perfil</p>
			            			<div class="fileupload fileupload-new" data-provides="fileupload">
				                        <div class="fileupload-preview thumbnail form-control">
				                        	<c:if test="${ funcionario.imagem.id ne null }">
				                        		<img alt="perfil" src="/min/web/upload/${ funcionario.imagem.id }" />
				                        	</c:if>
				                        </div>
				                        
				            			<c:if test="${ canEdit }">
					                        <div>
					                            <span class="btn btn-file btn-alt btn-sm">
					                                <span class="fileupload-new">Selecionar imagem</span>
					                                <span class="fileupload-exists">Alterar</span>
					                                <input type="file" name="imagem" id="imagem" />
					                            </span>
					                            <a href="#" class="btn fileupload-exists btn-sm" data-dismiss="fileupload">Remover</a>
					                        </div>
				            			</c:if>
				                    </div>
			                	
			                	 </div>
							</form>
							<c:if test="${ loggedUser.pessoa.id eq funcionario.id }">
								<div class="tile m-t-20 w-100-p">
									<h2 class="tile-title">Próximas tarefas</h2>
									<div class="tile-config dropdown">
										<a data-toggle="dropdown" href="" class="tile-menu"></a>
										<ul class="dropdown-menu animated pull-right text-right">
											<li id="todo-add"><a href="/min/web/tarefas/">Ver todas/Enviar mensagens</a></li>
											<li id="todo-refresh"><a href="javascript:Home.exibirProximasTarefas()">Atualizar</a></li>
										</ul>
									</div>
		
									<div class="listview overflow" id="tarefas-todo" style="height: 400px">
									
									</div>
		
								</div>
							</c:if>
	           			</div>
	           			<div class="col-md-9">
	           				<c:if test="${ canEdit }">
					            <div class="col-lg-12 m-b-10" id="text-input">
					            		<p>Preencha os dados do funcionário e clique em Salvar</p>
					            </div>
			            	</c:if>
							<div class="col-lg-12 m-b-10" id="buttons">
				            	<c:if test="${ canEdit }">
					               <a class="btn m-r-5" id="salvar">Salvar</button>
						        </c:if>
				               <a href="<spring:url value='/web/funcionarios/' />" class="btn btn-alt m-r-5" type="button">Cancelar</a>
				           	</div>
	     			      	<h4 class="page-title m-b-10">DADOS DE USUÁRIO</h4>
				            <form:form method="post" action="../salvar" commandName="funcionario" id="funcionario-form">
				               	<form:hidden path="usuario.id" />
				                <div class="col-lg-12 p-l-0">
					                <div class="col-lg-6">
				                 	<form:input path="usuario.login" cssClass="form-control m-b-10" placeholder="Nome de usuário" readonly="${ not canEdit }"/>
					                </div>
				                </div>
				                
						        <c:if test="${ canEdit }">
					                <div class="col-lg-12 p-l-0">
						                <div class="col-lg-6">
					                 	<form:password path="usuario.senha" cssClass="form-control m-b-10" placeholder="Senha" readonly="${ not canEdit }"/>
						                </div>
					                </div>
					                <div class="col-lg-12 p-l-0">
						                <div class="col-lg-6">
					                 	<form:password path="usuario.confirmacaoSenha" cssClass="form-control m-b-10" placeholder="Confirmação de senha" readonly="${ not canEdit }"/>
						                </div>
					                </div>
				                </c:if>
				                <div class="col-lg-12 p-l-0">
					                <div class="col-lg-6">
				                	<form:select path="usuario.role" cssClass="form-control m-b-10"  disabled="${ not hasRole['ADMIN'] }">
				                         <form:option value="ADMIN">Administrador do sistema</form:option>
				                         <form:option value="CAIXA">Caixa</form:option>
				                         <form:option value="OPERACIONAL">Operacional</form:option>
				                	</form:select>
					                </div>
				                </div>
				                <div class="col-lg-12">
									<h4 class="page-title m-b-10">INFORMAÇÕES</h4>
								</div>
				            	<form:hidden path="id" id="funcionario-id" />
				            	<form:hidden path="imagem.id" id="imagem-id" />
				                 <div class="col-lg-12">
				                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                	<form:select path="funcaoPrincipal" cssClass="form-control m-b-10"  disabled="${ not hasRole['ADMIN'] }">
				                         <form:option value="Recepcionista">Recepcionista</form:option>
				                         <form:option value="Manicure">Manicure</form:option>
				                         <form:option value="Cabelereira">Cabelereira</form:option>
				                         <form:option value="Maquiadora">Maquiadora</form:option>
				                         <form:option value="Administrador">Administrador</form:option>
				                	</form:select>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="telefone" cssClass="form-control m-b-10" placeholder="Telefones"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="aniversarioStr" cssClass="form-control m-b-10 mask-date validate[required,custom[date]]" placeholder="Aniversário (dd/MM)"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="email" cssClass="form-control m-b-10" placeholder="E-mail"  readonly="${ not canEdit }"/>
				                </div>
				                
				                <div class="col-lg-6">
				                 	<form:input path="endereco" cssClass="form-control m-b-10" placeholder="Endereço"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="cep" cssClass="form-control m-b-10 mask-cep" placeholder="CEP"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="cidade" cssClass="form-control m-b-10" placeholder="Cidade"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<form:input path="documento" cssClass="form-control m-b-10" placeholder="RG ou CPF"  readonly="${ not canEdit }"/>
				                </div>
				                <div class="col-lg-3">
				                 	<div class="color-pick input-icon">
				                         <form:input path="cor" cssClass="form-control color-picker m-b-10" placeholder="Escolha uma cor..."   readonly="${ not canEdit }"/>
				                         <span class="color-preview"></span>
				                         <span class="add-on">
				                             <i class="sa-plus"></i>
				                         </span>
				                     </div>
				                </div>
				                
				                <c:if test="${ canEdit }">
					                <div class="row">
					               		<div class="col-lg-12">
					                		<h4 class="page-title m-b-10">COMISSÂO</h4>
					                	</div>
					                	<div class="col-lg-12">
					                		<div class="col-lg-3">
						                		<p>Serviço</p>
					                 			<input id="comissao.comissaoServico" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoServico"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoServico }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                	</div>
					                	<div class="col-lg-12">
					                		<div class="col-lg-3">
						                		<p>Auxiliar</p>
					                 			<input id="comissao.comissaoAuxiliar"
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoAuxiliar"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoAuxiliar }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                	</div>
					                	<div class="col-lg-12">
					                		<div class="col-lg-3">
						                		<p>Vendas até (R$)</p>
					                 			<input id="comissao.valorRange1" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.valorRange1"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange1 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
					                 			<input id="comissao.comissaoRange1" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoRange1"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange1 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                		<div class="col-lg-3">
						                		<p>Vendas até (R$)</p>
					                 			<input id="comissao.valorRange2" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.valorRange2"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange2 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
					                 			<input id="comissao.comissaoRange2" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoRange2"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange2 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                		<div class="col-lg-3">
						                		<p>Vendas até (R$)</p>
					                 			<input id="comissao.valorRange3" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.valorRange3"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange3 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
					                 			<input id="comissao.comissaoRange3" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoRange3"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange3 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                		<div class="col-lg-3">
						                		<p>Comissão máxima (%)</p>
					                 			<input id="comissao.comissaoRange4" 
					                 						<c:if test="${ not hasRole['ADMIN'] }">readonly="readonly"</c:if>
					                 						name="comissao.comissaoRange4"
					                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange4 }" minFractionDigits="2"  />" 
					                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
					                		</div>
					                	</div>
					                </div>
					                
				                </c:if>
				             </form:form>
	           			</div>
					</div>
           		</div>
             
              <div class="clearfix"></div>
            <br /><br />
            <c:if test="${ funcionario.id ne null }">
            	
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#employee-menu").addClass("active");
		$("#admin-menu").addClass("active");

		$(document).ready(function(){
             $('.mask-date').mask('00/00');
             $('.mask-cep').mask('00000-000');
             $('.mask-number').mask('########0');
             $('.mask-percent').mask('##0,00%', {reverse: true});
             $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
             $("#salvar").on('click', function(){
                 Funcionario.submit();
               });
		});
		
	</script>
	<c:forEach items=""></c:forEach>
	<script type="text/javascript">
		<c:forEach var="error" items="${ errorMessages }">
			Utils.showError("${error}");
		</c:forEach> 
		Home.isHome = true;
		Home.exibirProximasTarefas();
	</script>
</body>
</html>
