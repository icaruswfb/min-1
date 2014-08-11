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

			<h4 class="page-title">SERVIÇO</h4>
            <form:form method="post" action="../salvar" commandName="servico" id="servico-form">
            	<form:hidden path="id" />
				<div class="block-area" id="buttons">
	                 <a class="btn m-r-5" onclick="Servico.submit()">Salvar</a>
	                 <a href="<spring:url value='/web/servicos/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do serviço e clique em Salvar</p>
                </div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-2">
                 	<form:input path="duracaoMinutos" cssClass="form-control m-b-10 mask-number" placeholder="Duração em minutos"/>
                </div>
                <div class="col-lg-2">
                 	<input name="preco" class="form-control m-b-10 mask-money" id="preco" placeholder="Preço sugerido" value="<fmt:formatNumber minFractionDigits="2" value="${ servico.preco }" />"/>
                </div>
                
             </form:form>
              <div class="clearfix"></div>
            <br /><br />
            <c:if test="${ servico.id ne null }">
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
            </c:if>
            
		</section>












		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#servicos-menu").addClass("active");
		$("#admin-menu").addClass("active");

		$(document).ready(function(){
             $('.mask-number').mask('##0');
             $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		});

		Servico = {
			submit:function(){
				Utils.unmaskMoney();
				$("#servico-form").submit();
			}
		};
		
	</script>
	
</body>
</html>
