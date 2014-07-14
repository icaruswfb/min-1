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
	            <div class="block-area">
						<div id="comandas" class="row">
							<form action="" method="post" id="comanda-form">
								
							</form>
						</div>
				</div>
             	 <div class="clearfix"></div>
           		 <br />
				<h4 class="page-title">HISTÓRICO</h4>
	            <div class="block-area">
					<div class="row">
						<div class="col-md-9">
							<!-- Main Chart -->
							<div class="tile">
								<h2 class="tile-title">Visitas x Mês</h2>
								<div class="p-10">
									<div id="line-chart" class="main-chart" style="height: 100px"></div>
	
								</div>
							</div>
						</div>
						
						<div class="col-md-3">
							<div class="tile">
	                            <h2 class="tile-title">DADOS COMPILADOS</h2>
	                            <div class="listview narrow">
	                                <div class="media p-l-5">
	                                    <div class="media-body">
	                                        <small class="text-muted">Total de visitas:</small><br/>
	                                        10
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="listview narrow">
	                                <div class="media p-l-5">
	                                    <div class="media-body">
	                                        <small class="text-muted">Quantia total gasta:</small><br/>
	                                        R$1.320,00
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="listview narrow">
	                                <div class="media p-l-5">
	                                    <div class="media-body">
	                                        <small class="text-muted">Quantia média gasta por visita:</small><br/>
	                                        R$132,00
	                                    </div>
	                                </div>
	                            </div>
	                        </div>
						</div>​
						
					</div>
	
					<div class="clearfix"></div>
				</div>
				<div class="block-area" id="historico">
					<div class="listview list-container">
	                    <c:forEach var="historico" items="${ historicos }">
		                    <div class="media">
									<small class="text-muted">${historico.textoPequeno}</small><br />
		                        <div class="media-body">
		                        	<a href="/min/web/funcionarios/editar/${ historico.funcionario.id }">${ historico.funcionario.nome }</a>:
		                            ${ historico.texto }
		                            <!-- 
		                            <div class="list-options">
		                                <button class="btn btn-sm">View</button>
		                                <button class="btn btn-sm">Delete</button>
		                            </div>
		                             -->
		                        </div>
		                    </div>
	                    </c:forEach>           
	                </div>
				</div>
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#clientes-menu").addClass("active");

		$(document).ready(function(){
             $('.mask-date').mask('00/00');
             $('.mask-cep').mask('00000-000');
		});
		Comanda.init();
        <c:if test="${ cliente.id ne null }">
        	Comanda.findComandas();
        </c:if>
	</script>
	
</body>
</html>
