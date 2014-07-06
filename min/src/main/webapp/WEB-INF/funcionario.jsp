<!DOCTYPE html>
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="template/head.jsp"></jsp:include>
<body id="skin-blur-greenish">
	<jsp:include page="template/header.jsp"></jsp:include>

	<div class="clearfix"></div>

	<section id="main" class="p-relative" role="main">


		<jsp:include page="template/sidebar.jsp"></jsp:include>

		<!-- Content -->
		<section id="content" class="container">

			<h4 class="page-title">FUNCIONÁRIO</h4>
            <form:form method="post" action="../salvar" commandName="funcionario">
            	<form:hidden path="id" />
				<div class="block-area" id="buttons">
	                 <button class="btn m-r-5" type="submit">Salvar</button>
	                 <a href="<spring:url value='/web/funcionarios/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do funcionário e clique em Salvar</p>
                </div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-3">
                	<form:select path="funcaoPrincipal" cssClass="select custom-select">
                         <form:option value="Recepcionista">Recepcionista</form:option>
                         <form:option value="Manicure">Manicure</form:option>
                         <form:option value="Cabelereira">Cabelereira</form:option>
                         <form:option value="Maquiadora">Maquiadora</form:option>
                	</form:select>
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
            <br /><br />
            <c:if test="${ funcionario.id ne null }">
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
	            <div class="clearfix"></div>
				<h4 class="page-title">HISTÓRICO</h4>
				<div class="block-area" id="historico">
					<div class="listview list-container">
	                     <header class="listview-header media">
	                        <input type="checkbox" class="pull-left list-parent-check" value="">
	                        
	                        <ul class="list-inline list-mass-actions pull-left">
	                            <li>
	                                <a data-toggle="modal" href="#compose-message" title="Add" class="tooltips">
	                                    <i class="sa-list-add"></i>
	                                </a>
	                            </li>
	                            <li>
	                                <a href="" title="Refresh" class="tooltips">
	                                    <i class="sa-list-refresh"></i>
	                                </a>
	                            </li>
	                            <li class="show-on" style="display: none;">
	                                <a href="" title="Delete" class="tooltips">
	                                    <i class="sa-list-delete"></i>
	                                </a>
	                            </li>
	                        </ul>
	
	                        <div class="clearfix"></div>
	                    </header>                   
	                    <div class="media">
								<small class="text-muted">31/12/2013 18:30 com <a href="">Susy</a></small><br />
	                        <input type="checkbox" class="pull-left list-check" value="">
	                        <div class="media-body">
	                            Per an error perpetua, fierent fastidii recteque ad pro. Mei id brute intellegam
	                            <div class="list-options">
	                                <button class="btn btn-sm">View</button>
	                                <button class="btn btn-sm">Delete</button>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="media">
								<small class="text-muted">18:30 com <a href="">Susy</a></small><br />
	                        <input type="checkbox" class="pull-left list-check" value="">
	                        <div class="media-body">
	                            Per an error perpetua, fierent fastidii recteque ad pro. Mei id brute intellegam
	                            <div class="list-options">
	                                <button class="btn btn-sm">View</button>
	                                <button class="btn btn-sm">Delete</button>
	                            </div>
	                        </div>
	                    </div>
	                </div>
				</div>
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#employee-menu").addClass("active");

		$(document).ready(function(){
             $('.mask-date').mask('00/00');
             $('.mask-cep').mask('00000-000');
		});
		
	</script>
	
</body>
</html>
