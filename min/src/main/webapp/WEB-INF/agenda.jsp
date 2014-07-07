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

			<h4 class="page-title">NOVO HORÁRIO</h4>
				<form >

					<!-- Novo horário -->
						<div class="block-area" >
			                 <button class="btn m-r-5" type="button" onclick="addHorario()">Salvar</button>
				             <button class="btn btn-alt m-r-5" type="button">Limpar</button>
			             </div>
			             
			              <div class="clearfix"></div>
			            <br />
			             <div class="col-lg-3" >
			                 <label>Cliente</label>
			                 <select data-placeholder="Select Users..." class="tag-select-limited" multiple="multiple" id="cliente-select">
			                 	<c:forEach var="cliente" items="${ pessoas }">
			                 		<c:if test="${ cliente.funcionario eq false }">
				                 		<option value="${ cliente.id }">${ cliente.nome } [${cliente.id }]</option>
			                 		</c:if>
			                 	</c:forEach>
		                    </select>
			             </div>
			             <div class="col-lg-3" >
			                 <label>Funcionário</label>
			                  <select data-placeholder="Select Users..." class="tag-select-limited" multiple="multiple" id="funcionario-select">
			                 	<c:forEach var="funcionario" items="${ pessoas }">
			                 		<c:if test="${ funcionario.funcionario eq true }">
				                 		<option value="${ funcionario.id }">${ funcionario.nome }</option>
			                 		</c:if>
			                 	</c:forEach>
		                    </select>
			             </div>
			             <div class="col-lg-2" >
			                 <label>Data</label>
			                 <div class="input-icon datetime-pick date-only">
                                <input data-format="dd/MM/yyyy" type="text" class="form-control input-sm"  id="dia-agenda"/>
                                <span class="add-on">
                                    <i class="sa-plus"></i>
                                </span>
                            </div>
			             </div>
			             <div class="col-lg-2" >
			                 <label>Horário</label>
			                 <div class="input-icon datetime-pick time-only">
                                <input data-format="hh:mm" type="text" class="form-control input-sm" id="horario-inicio-agenda"/>
                                <span class="add-on">
                                    <i class="sa-plus"></i>
                                </span>
                            </div>
			             </div>
			              <div class="col-lg-2" >
			                 <label>Horário de término</label>
			                 <div class="input-icon datetime-pick time-only">
                                <input data-format="hh:mm" type="text" class="form-control input-sm" id="horario-fim-agenda"/>
                                <span class="add-on">
                                    <i class="sa-plus"></i>
                                </span>
                            </div>
			             </div>
				</form>
              <div class="clearfix"></div>
            <br /><br />
			<h4 class="page-title">AGENDA</h4>

			<div class="block-area">
				
				<div class="row">
					<!-- Calendario do dia -->
					<div class="col-md-12 clearfix">

						<div id="calendar" class="p-relative p-10 m-b-20">
							<!-- Calendar Views -->
							<ul class="calendar-actions list-inline clearfix">
								<li class="p-r-0"><a data-view="month" href="#"
									class="tooltips" title="Month"> <i class="sa-list-month"></i>
								</a></li>
								<li class="p-r-0"><a data-view="agendaWeek" href="#"
									class="tooltips" title="Week"> <i class="sa-list-week"></i>
								</a></li>
								<li class="p-r-0"><a data-view="agendaDay" href="#"
									class="tooltips" title="Day"> <i class="sa-list-day"></i>
								</a></li>
							</ul>
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
					/* Tag Select */
	                (function(){
	                    /* Limited */
	                    $(".tag-select-limited").chosen({
	                        max_selected_options: 1
	                    });
	                    
	                    /* Overflow */
	                    $('.overflow').niceScroll();
	                })();
	                
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
		$("#agenda-menu").addClass("active");
	</script>
	
</body>
</html>
