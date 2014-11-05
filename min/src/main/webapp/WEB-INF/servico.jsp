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
                <div class="clearfix"></div>
                 <div class="col-lg-12">
                 	<form:input path="nome" cssClass="form-control input-lg m-b-10" placeholder="Nome"/>
                </div>
                <div class="col-lg-2">
               		 <p>Duração</p>
                 	<form:input path="duracaoMinutos" cssClass="form-control m-b-10 mask-number" placeholder="Duração em minutos"/>
                </div>
                <div class="col-lg-2">
               		 <p>Tempo de ação do produto</p>
                 	<form:input path="tempoAcaoProdutoMinutos" cssClass="form-control m-b-10 mask-number" placeholder="Tempo de ação do produto"/>
                </div>
                <div class="col-lg-2">
                <p>Preço</p>
                 	<input name="preco" class="form-control m-b-10 mask-money" id="preco" placeholder="Preço sugerido" value="<fmt:formatNumber minFractionDigits="2" value="${ servico.preco }" />"/>
                </div>
                <div class="col-lg-3">
                <p>Comissão do serviço</p>
                	<form:select path="comicionado" cssClass="form-control m-b-10" >
						<form:option value="true" label="Serviço comicionado" />                		
						<form:option value="false" label="Serviço não comicionado" />
                	</form:select>
                </div>
                <!-- 
                 -->
                <div class="col-lg-3">
                <p>Tipo de Serviço</p>
                	<select name="tipoServicoId" class="form-control m-b-10" >
                		<c:forEach var="tipoServico" items="${ tiposServico }">
                			<c:choose>
                			<c:when test="${ servico.tipoServico.id eq tipoServico.id }">
	                			<option selected="selected" value="${ tipoServico.id }">${ tipoServico.nome }</option>
                			</c:when>
                			<c:otherwise>
	                			<option value="${ tipoServico.id }">${ tipoServico.nome }</option>
                			</c:otherwise>
                			</c:choose>
                		</c:forEach>
                	</select>
                </div>
                
             </form:form>
              <div class="clearfix"></div>
            <br /><br />
            <c:if test="${ servico.id ne null }">
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
