<!DOCTYPE html>
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="template/head.jsp"></jsp:include>
<body id="skin-tectile">
	
	<jsp:include page="template/header.jsp"></jsp:include>

	<div class="clearfix"></div>

	<section id="main" class="p-relative" role="main">


		<jsp:include page="template/sidebar.jsp"></jsp:include>




		<!-- Content -->
		<section id="content" class="container">

			<h4 class="page-title">HOME</h4>

			<div class="block-area">
				<div class="row">
					<div class="col-md-12">
						<!-- Main Chart -->
						<div class="tile">
							<h2 class="tile-title">Estatísticas Clientes x Semana</h2>
							<div class="tile-config dropdown">
								<a data-toggle="dropdown" href="" class="tile-menu"></a>
								<ul class="dropdown-menu animated pull-right text-right">
									<li><a class="tile-info-toggle" href="">Chart
											Information</a></li>
									<li><a href="">Refresh</a></li>
									<li><a href="">Settings</a></li>
								</ul>
							</div>
							<div class="p-10">
								<div id="line-chart" class="main-chart" style="height: 250px"></div>

								<div class="chart-info">
									<ul class="list-unstyled">
										<li class="m-b-10">Total Sales 1200 <span
											class="pull-right text-muted t-s-0"> <i
												class="fa fa-chevron-up"></i> +12%
										</span>
										</li>
										<li><small> Local 640 <span
												class="pull-right text-muted t-s-0"><i
													class="fa m-l-15 fa-chevron-down"></i> -8%</span>
										</small>
											<div class="progress progress-small">
												<div class="progress-bar progress-bar-warning"
													role="progressbar" aria-valuenow="40" aria-valuemin="0"
													aria-valuemax="100" style="width: 40%"></div>
											</div></li>
										<li><small> Foreign 560 <span
												class="pull-right text-muted t-s-0"> <i
													class="fa m-l-15 fa-chevron-up"></i> -3%
											</span>
										</small>
											<div class="progress progress-small">
												<div class="progress-bar progress-bar-info"
													role="progressbar" aria-valuenow="40" aria-valuemin="0"
													aria-valuemax="100" style="width: 60%"></div>
											</div></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="clearfix"></div>
			</div>

			<div class="block-area">
				<div class="row">
					<!-- Calendario do dia -->
					<div class="col-md-9 clearfix">

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

					<!-- Tasks TO DO -->
					<div class="col-md-3">
						<div class="tile">
							<h2 class="tile-title">Tasks to do</h2>
							<div class="tile-config dropdown">
								<a data-toggle="dropdown" href="" class="tile-menu"></a>
								<ul class="dropdown-menu animated pull-right text-right">
									<li id="todo-add"><a href="">Add New</a></li>
									<li id="todo-refresh"><a href="">Refresh</a></li>
									<li id="todo-clear"><a href="">Clear All</a></li>
								</ul>
							</div>

							<div class="listview todo-list sortable">
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox">
											Curabitur quis nisi ut nunc gravida suscipis
										</label>
									</div>
								</div>
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox">
											Suscipit at feugiat dewoo
										</label>
									</div>

								</div>
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox">
											Gravida wendy lorem ipsum seen
										</label>
									</div>

								</div>
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox">
											Fedrix quis nisi ut nunc gravida suscipit at feugiat purus
										</label>
									</div>

								</div>
							</div>

							<h2 class="tile-title">Completed Tasks</h2>

							<div class="listview todo-list sortable">
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox"
											checked="checked"> Motor susbect win latictals bin
											the woodat cool
										</label>
									</div>

								</div>
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox"
											checked="checked"> Wendy mitchel susbect win
											latictals bin the woodat cool
										</label>
									</div>

								</div>
								<div class="media">
									<div class="checkbox m-0">
										<label class="t-overflow"> <input type="checkbox"
											checked="checked"> Latictals bin the woodat cool for
											the win
										</label>
									</div>

								</div>
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
	</script>
	
</body>
</html>
