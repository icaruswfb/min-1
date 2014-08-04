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

			<h4 class="page-title">MENSAGENS/TAREFAS</h4>
			
                <div class="listview list-container">
                    <header class="listview-header media">
                    	<div class="checkbox-mensagem tooltips" onclick="Tarefa.lerTodas()" title="Ler todas" ></div>
                            
                        <ul class="list-inline list-mass-actions pull-left">
                            <li>
                                <a data-toggle="modal" href="#modalDefault"  title="Nova" class="tooltips" onclick="Tarefa.limparFormulario()">
                                    <i class="sa-list-add"></i>
                                </a>
                            </li>
                            <li>
                                <a href="" title="Atualizar" class="tooltips" >
                                    <i class="sa-list-refresh"></i>
                                </a>
                            </li>
                        </ul>

                        <input class="input-sm col-md-4 pull-right message-search" type="text" placeholder="Pesquisar..." id="pesquisa-tarefa" />
                        
                        <div class="clearfix"></div>
                    </header>
                    
                    <div id="tarefas-block">
                    </div>
                    
                    
                </div>
				
				
                    <div class="modal fade" id="modalDefault" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">Nova mensagem/tarefa</h4>
                                </div>
                                <div class="modal-body">
                                	<form method="post" action="/min/web/tarefas/salvar" id="tarefa-form" >
                                		<input name="id" id="tarefa-id" type="hidden" />
                                		<label>Para (funcionário):</label>
						                 <select data-placeholder="Todos" class="tag-select-limited m-b-10" multiple="multiple" id="funcionario-select" name="funcionario">
						                 	<c:forEach var="funcionario" items="${ funcionarios }">
						                 		<option value="${ funcionario.id }">${ funcionario.nome } [${funcionario.id }]</option>
						                 	</c:forEach>
					                    </select>
					                    
                                		<label class="m-t-10">Referente (cliente):</label>
						                 <select data-placeholder="Nenhum" class="tag-select-limited m-b-10" multiple="multiple" id="cliente-select" name="cliente">
						                 	<c:forEach var="cliente" items="${ clientes }">
						                 		<option value="${ cliente.id }">${ cliente.nome } [${cliente.id }]</option>
						                 	</c:forEach>
					                    </select>
                                		<label class="m-t-10">Para o dia:</label>
						                 <div class="input-icon datetime-pick date-only m-b-10">
			                                <input name="dataAgendadaStr"  data-format="dd/MM/yyyy" class="form-control overflow" id="agendamento" />
			                                <span class="add-on">
			                                    <i class="sa-plus"></i>
			                                </span>
			                            </div>
                                		<textarea class="form-control auto-size" 
                                			placeholder="Mensagem" 
                                			style="overflow: hidden; word-wrap: break-word; resize: none; height: 52px;"
                                			name="descricao" id="descricao"></textarea>
                                	</form>
                                </div>
                                <div class="modal-footer">
                                    <a class="btn btn-sm" id="criar-tarefa-button" data-dismiss="modal">Criar</a>
                                    <button type="button" class="btn btn-sm" data-dismiss="modal">Fechar</button>
                                </div>
                            </div>
                        </div>
                    </div>
				
		</section>
	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	
	<script type="text/javascript">

	$(document).ready(
			function() {
				Tarefa.exibirTodas();
				$("#criar-tarefa-button").on('click', function(){
					Tarefa.salvarTarefa();
				});
				$("#pesquisa-tarefa").on('keyup', function(){
					Tarefa.pesquisar();
				});
			});
	$(document).ready(
			function() {
				/* Tag Select */
                (function(){
                    /* Limited */
                    $(".tag-select-limited").chosen({
                        max_selected_options: 1
                    });
                    /* Overflow */
                    $('.overflow').niceScroll();
                })();
                
			});
	</script>
	
</body>
</html>
