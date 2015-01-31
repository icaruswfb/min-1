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
			
			<h4 class="page-title">HOME</h4>

			<div class="block-area">
				<div class="row">
					<!-- Calendario do dia -->
					<div class="col-md-8 clearfix">

						<div class="jumbotron tile-light">
                          <div class="container">
                               <h1>Olá, ${ loggedUser.pessoa.nome }!</h1>
                               <p><a class="btn btn-alt btn-lg" href="/min/web/agenda/">Agenda de hoje</a></p>
                          </div>
	                     </div>
					</div>

					<!-- Tasks TO DO -->
					<div class="col-md-4">
						<div class="tile">
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
					</div>

				</div>

				<div class="clearfix"></div>
			</div>


		</section>












	</section>

	<jsp:include page="template/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$(document).ready(
				function() {
					var date = new Date();
					var d = date.getDate();
					var m = date.getMonth();
					var y = date.getFullYear();
					$('#calendar').fullCalendar(
							{
								lang: 'pt-br',
								header : {
									center : 'title',
									left : 'prev, next',
									right : ''
								},

								selectable : true,
								selectHelper : true,
								editable : true,
								events : [ {
									title : 'Hangout with friends',
									start : new Date(y, m, 1),
									end : new Date(y, m, 2)
								} ],

								//On Day Select
								select : function(start, end, allDay) {
									$('#addNew-event').modal('show');
									$('#addNew-event input:text').val('');
									$('#getStart').val(start);
									$('#getEnd').val(end);
								},

								eventResize : function(event, dayDelta,
										minuteDelta, revertFunc) {
									$('#editEvent').modal('show');

									var info = "The end date of " + event.title
											+ "has been moved " + dayDelta
											+ " days and " + minuteDelta
											+ " minutes.";

									$('#eventInfo').html(info);

									$('#editEvent #editCancel').click(
											function() {
												revertFunc();
											})
								}
							});

					$('body').on(
							'click',
							'#addEvent',
							function() {
								var eventForm = $(this).closest('.modal').find(
										'.form-validation');
								eventForm.validationEngine('validate');

								if (!(eventForm).find('.formErrorContent')[0]) {

									//Event Name
									var eventName = $('#eventName').val();

									//Render Event
									$('#calendar').fullCalendar('renderEvent',
											{
												title : eventName,
												start : $('#getStart').val(),
												end : $('#getEnd').val(),
												allDay : true,
											}, true); //Stick the event

									$('#addNew-event form')[0].reset()
									$('#addNew-event').modal('hide');
								}
							});
					$('#calendar').fullCalendar('changeView', 'agendaDay');
					var overflowRegular, overflowInvisible = false;
					overflowRegular = $('.overflow').niceScroll();
				});

		//Calendar views
		$('body').on('click', '.calendar-actions > li > a', function(e) {
			e.preventDefault();
			var dataView = $(this).attr('data-view');
			$('#calendar').fullCalendar('changeView', dataView);

			//Custom scrollbar
			var overflowRegular, overflowInvisible = false;
			overflowRegular = $('.overflow').niceScroll();
		});
	</script>
	
	<script type="text/javascript">
		$("#home-menu").addClass("active");
		Home.isHome = true;
		Home.exibirProximasTarefas();
	</script>
	
</body>
</html>
