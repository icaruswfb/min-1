<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Sidebar -->
<aside id="sidebar">

	<!-- Sidbar Widgets -->
	<div class="side-widgets overflow">
		<!-- Profile Menu -->
		<div class="text-center s-widget m-b-25 dropdown" id="profile-menu">
			<div style="height: 10px;  margin: 10px; background-color: ${ loggedUser.pessoa.cor }"></div>
			<input type="hidden" id="usuario-logado-id" value="${ loggedUser.id }"/>
			<input type="hidden" id="usuario-logado-pessoa-id" value="${ loggedUser.pessoa.id }"/>
			<a href="" data-toggle="dropdown"> <img
				class="profile-pic animated" src="<spring:url value='/web/upload/${ loggedUser.pessoa.imagem.id }' />" alt="">
			</a>
			<ul class="dropdown-menu profile-menu animated">
				<li>
					<a href="/min/web/funcionarios/editar/${ loggedUser.pessoa.id }">Meu perfil</a> 
					<i class="icon left">&#61903;</i>
					<i class="icon right">&#61815;</i>
				</li>
				<li>
					<a href="/min/web/tarefas/">Notificações</a> 
					<i class="icon left">&#61903;</i>
					<i 	class="icon right">&#61815;</i>
				</li>
				<li>
					<a href="/min/web/logout">Sair</a> 
					<i class="icon left">&#61903;</i>
					<i 	class="icon right">&#61815;</i>
				</li>
			</ul>
			<h4 class="m-0">${ loggedUser.pessoa.nome }</h4>
			${ loggedUser.login }
		</div>

		<!-- Calendar -->
		<div class="s-widget m-b-25">
			<div id="sidebar-calendar"></div>
		</div>

		<!-- Horários -->
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Próximos horários</h2>

			<div class="s-widget-body" id="feed-agenda">
			</div>
		</div>
		<!-- Feeds 
		<div class="s-widget m-b-25">
			<h2 class="tile-title">Notícias</h2>

			<div class="s-widget-body">
				<div id="news-feed"></div>
			</div>
		</div>
		-->

	</div>

	<!-- Side Menu -->
	<ul class="list-unstyled side-menu">
		<li id="home-menu"><a class="sa-side-home" href="<spring:url value='/web/' />">
				<span class="menu-item">Home</span>
		</a></li>
		<li  id="clientes-menu" ><a class="sa-side-form" href="<spring:url value='/web/clientes/' />"> <span
				class="menu-item">Clientes</span>
		</a></li>
		<li id="agenda-menu"><a class="sa-side-calendar" href="<spring:url value='/web/agenda/' />"> <span
				class="menu-item">Agenda</span>
		</a></li>
		<li class="dropdown" id="financeiro-menu">
             <a class="sa-side-chart" href="">
                 <span class="menu-item">Rolatórios</span>
             </a>
             <ul class="list-unstyled menu-item">
             	<li><a class="m-t-5"><i>Financeiro</i></a></li>
				<li><hr style="border-color: rgba(255,255,255,0.3)" /></li>
	             <c:if test="${ hasRole['ADMIN'] or hasRole['CAIXA'] }">
    	         	<li><a id="caixa-menu" href="<spring:url value='/web/report/caixa' />">Caixa do dia</a></li>
    	         	<li><a id="faturamento-menu" href="<spring:url value='/web/report/faturamento' />">Faturamento</a></li>
    	         	<!-- 
    	         	 -->
             	</c:if>
             	<c:if test="${ hasRole['ADMIN'] }">
   	         		<li><a id="fluxo-menu" href="<spring:url value='/web/report/fluxoFinanceiro' />">Fluxo financeiro</a></li>
   	         		 <li><a id="comandas-menu" href="<spring:url value='/web/report/comandas' />">Comandas</a></li>
   	         		<!-- 
   	         		 -->
   	         	</c:if>
				<li><a id="comissao-menu" href="<spring:url value='/web/report/comissao' />">Comissão</a></li>
             	<li><a class="m-t-5"><i>Outros</i></a></li>
				<li><hr style="border-color: rgba(255,255,255,0.3)" /></li>
         		 <li><a id="report-servicos-menu" href="<spring:url value='/web/report/servicos' />">Serviços</a></li>
         		 <li><a id="report-clientes-menu" href="<spring:url value='/web/report/clientes' />">Clientes</a></li>
				
             </ul>
         </li>
	         <li class="dropdown" id="admin-menu">
	             <a class="sa-side-folder" href="">
	                 <span class="menu-item">Salão</span>
	             </a>
	             <ul class="list-unstyled menu-item">
         			<c:if test="${ hasRole['ADMIN'] }">
		             	<li><a id="servicos-menu" href="<spring:url value='/web/servicos/' />">Serviços</a></li>
						<li><a id="produtos-menu" href="<spring:url value='/web/produtos/' />">Produtos</a></li>
		             	<li><a id="kits-menu" href="<spring:url value='/web/kits/' />">Kits</a></li>
						<li><a id="estoque-menu" href="<spring:url value='/web/estoque/' />">Projeção de estoque</a></li>
						<li><a id="produtos-precos-menu" href="<spring:url value='/web/produtos/precos' />">Preços de produtos</a></li>
			         </c:if>
					<li><a id="employee-menu" href="<spring:url value='/web/funcionarios/' />">Funcionários</a></li>
	             </ul>
	         </li>
		
		
		
	</ul>

</aside>