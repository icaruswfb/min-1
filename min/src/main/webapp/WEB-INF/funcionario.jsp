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

			<h4 class="page-title">FUNCIONÁRIO</h4>
            <form:form method="post" action="../salvar" commandName="funcionario" id="funcionario-form">
            	<form:hidden path="id" />
				<div class="block-area" id="buttons">
	                 <a class="btn m-r-5" id="salvar">Salvar</button>
	                 <a href="<spring:url value='/web/funcionarios/' />" class="btn btn-alt m-r-5" type="button">Cancelar</a>
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
                <div class="col-lg-3">
                 	<form:input path="documento" cssClass="form-control m-b-10" placeholder="RG ou CPF"/>
                </div>
                <div class="col-lg-3">
                 	<div class="color-pick input-icon">
                         <form:input path="cor" cssClass="form-control color-picker m-b-10" placeholder="Escolha uma cor..." />
                         <span class="color-preview"></span>
                         <span class="add-on">
                             <i class="sa-plus"></i>
                         </span>
                     </div>
                </div>
                
                <div class="col-lg-12">
               		<div class="col-lg-12">
                		<p>Percentuais de comissão</p>
                	</div>
                	<div class="col-lg-12">
                		<div class="col-lg-3">
	                		<p>Serviço</p>
                 			<input id="comissao.comissaoServico" 
                 						name="comissao.comissaoServico"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoServico }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                	</div>
                	<div class="col-lg-12">
                		<div class="col-lg-3">
	                		<p>Auxiliar</p>
                 			<input id="comissao.comissaoAuxiliar" 
                 						name="comissao.comissaoAuxiliar"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoAuxiliar }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                	</div>
                	<div class="col-lg-12">
                		<div class="col-lg-3">
	                		<p>Vendas até (R$)</p>
                 			<input id="comissao.valorRange1" 
                 						name="comissao.valorRange1"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange1 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
                 			<input id="comissao.comissaoRange1" 
                 						name="comissao.comissaoRange1"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange1 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                		<div class="col-lg-3">
	                		<p>Vendas até (R$)</p>
                 			<input id="comissao.valorRange2" 
                 						name="comissao.valorRange2"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange2 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
                 			<input id="comissao.comissaoRange2" 
                 						name="comissao.comissaoRange2"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange2 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                		<div class="col-lg-3">
	                		<p>Vendas até (R$)</p>
                 			<input id="comissao.valorRange3" 
                 						name="comissao.valorRange3"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.valorRange3 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-money" placeholder="Valor"/>
                 			<input id="comissao.comissaoRange3" 
                 						name="comissao.comissaoRange3"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange3 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                		<div class="col-lg-3">
	                		<p>Comissão máxima (%)</p>
                 			<input id="comissao.comissaoRange4" 
                 						name="comissao.comissaoRange4"
                 						value="<fmt:formatNumber value="${ funcionario.comissao.comissaoRange4 }" minFractionDigits="2"  />" 
                 						class="form-control m-b-10 mask-percent" placeholder="Percentual"/>
                		</div>
                	</div>
                </div>
             </form:form>
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
	
</body>
</html>
