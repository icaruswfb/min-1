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

			<h4 class="page-title">KIT (Serviços e produtos combinados)</h4>
            <form:form method="post" action="/min/web/kits/salvar" commandName="kit" id="kit-form">
            	<form:hidden path="id" />
				<div class="block-area" id="buttons">
	                 <a class="btn m-r-5" onclick="Kit.submit()">Salvar</a>
	                 <a href="<spring:url value='/web/kits/' />" >
		                 <button class="btn btn-alt m-r-5" type="button">Cancelar</button>
	                 </a>
	             </div>
			 	<!-- Table Hover -->
                <div class="block-area" id="text-input">
                	<p>Preencha os dados do kit e clique em Salvar</p>
                </div>
                <div class="clearfix"></div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-2">
                 	<input name="valor" class="form-control m-b-10 mask-money" id="preco" placeholder="Preço sugerido" value="<fmt:formatNumber minFractionDigits="2" value="${ kit.valor }" />"/>
                </div>
                
                <div class="clearfix"></div>
                <div class="col-lg-12">
                	<a href="javascript:Kit.addServico()" ><span class="add-kit" >Adicionar serviço</span></a><a href="javascript:Kit.addServico()" ><i class="sa-list-add add-kit-img"></i></a>
                </div>
                <div class="w-100 float-left" id="bloco-servicos">
                	
                </div>
                
             </form:form>
              <div class="clearfix"></div>
            <br /><br />
            
		</section>
		
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">
		$("#kits-menu").addClass("active");
		$("#admin-menu").addClass("active");

		$(document).ready(function(){
			Kit.init();            
             $('.mask-number').mask('##0');
             $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		});

	</script>
	
</body>
</html>
