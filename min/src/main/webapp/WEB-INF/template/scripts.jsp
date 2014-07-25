<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
	<!-- Javascript Libraries -->
	<!-- jQuery -->
	<script src="<spring:url value='/js/jquery.min.js' />"></script>
	<!-- jQuery Library -->
	<script src="<spring:url value='/js/jquery-ui.min.js' />"></script>
	<!-- jQuery UI -->
	<script src="<spring:url value='/js/jquery.easing.1.3.js' />"></script>
	<!-- jQuery Easing - Requirred for Lightbox + Pie Charts-->

	<!-- Bootstrap -->
	<script src="<spring:url value='/js/bootstrap.min.js' />"></script>
	
	<!--  Form Related -->
        <script src="<spring:url value='/js/validation/validate.min.js' />"></script> <!-- jQuery Form Validation Library -->
        <script src="<spring:url value='/js/validation/validationEngine.min.js' />"></script> <!-- jQuery Form Validation Library - requirred with above js -->
        <script src="<spring:url value='/js/select.min.js' />"></script> <!-- Custom Select -->
        <script src="<spring:url value='/js/chosen.min.js' />"></script> <!-- Custom Multi Select -->
        <script src="<spring:url value='/js/datetimepicker.min.js' />"></script> <!-- Date & Time Picker -->
        <script src="<spring:url value='/js/colorpicker.min.js' />"></script> <!-- Color Picker -->
        <script src="<spring:url value='/js/icheck.js' />"></script> <!-- Custom Checkbox + Radio -->
        <script src="<spring:url value='/js/autosize.min.js' />"></script> <!-- Textare autosize -->
        <script src="<spring:url value='/js/toggler.min.js' />"></script> <!-- Toggler -->
        <script src="<spring:url value='/js/input-mask.min.js' />"></script> <!-- Input Mask -->
        <script src="<spring:url value='/js/spinner.min.js' />"></script> <!-- Spinner -->
        <script src="<spring:url value='/js/slider.min.js' />"></script> <!-- Input Slider -->
        <script src="<spring:url value='/js/fileupload.min.js' />"></script> <!-- File Upload -->
	
        <!-- Text Editor -->
        <script src="<spring:url value='/js/editor.min.js' />"></script> <!-- WYSIWYG Editor -->
        <script src="<spring:url value='/js/markdown.min.js' />"></script> <!-- Markdown Editor -->
	<!-- Charts -->
	<script src="<spring:url value='/js/charts/jquery.flot.js' />"></script>
	<script src="<spring:url value='/js/charts/jquery.flot.time.js' />"></script>
	<script src="<spring:url value='/js/charts/jquery.flot.animator.min.js' />"></script>
	<script src="<spring:url value='/js/charts/jquery.flot.resize.min.js' />"></script>
    <script src="<spring:url value='/js/charts/jquery.flot.orderBar.js' />"></script> <!-- Flot Bar chart -->
    <script src="<spring:url value='/js/charts/jquery.flot.pie.min.js' />"></script> <!-- Flot Pie chart -->
	<!-- Flot sub - for repaint when resizing the screen -->

	<script src="<spring:url value='/js/sparkline.min.js' />"></script>
	<script src="<spring:url value='/js/easypiechart.js' />"></script>
	<script src="<spring:url value='/js/charts.js' />"></script>
	<!-- All the above chart related functions -->

	<!-- Map -->
	<script src="<spring:url value='/js/maps/jvectormap.min.js' />"></script>

	<!--  Form Related -->
	<script src="<spring:url value='/js/icheck.js' />"></script>
	<!-- Custom Checkbox + Radio -->

	<!-- UX -->
	<script src="<spring:url value='/js/scroll.min.js' />"></script>
	<!-- Custom Scrollbar -->

	<!-- Other -->
	<script src="<spring:url value='/js/calendar.min.js' />"></script>
	<script src="<spring:url value='/js/agenda.js' />"></script>
	<script src="<spring:url value='/js/utils.js' />"></script>
	<!-- Calendar -->
	<script src="<spring:url value='/js/feeds.min.js' />"></script>
	<!-- News Feeds -->


	<!-- All JS functions -->
	<script src="<spring:url value='/js/functions.js' />"></script>
	<script src="<spring:url value='/js/produto.js' />"></script>
	<script src="<spring:url value='/js/cliente.js' />"></script>
	<script src="<spring:url value='/js/comanda.js' />"></script>
	<script src="<spring:url value='/js/funcionario.js' />"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			Agenda.criarFeedAgenda();
		});
		var skins = [
								"skin-blur-violate",
								"skin-blur-blue",
								"skin-blur-chrome",
								"skin-blur-city",
								"skin-blur-greenish",
								"skin-blur-kiwi",
								"skin-blur-lights",
								"skin-blur-night",
								"skin-blur-ocean",
								"skin-blur-sunny",
								"skin-blur-sunset",
								"skin-blur-yellow",
								"skin-cloth",
								"skin-tectile"
		     		];
		//$("body").attr("id",skins[Math.floor(Math.random() * skins.length)]);
		$("body").attr("id",skins[0]);
	</script>
	
<!-- Older IE Message -->
		<!--[if
                <div class="ie-block">
                    <h1 class="Ops">Ooops!</h1>
                    <p>You are using an outdated version of Internet Explorer, upgrade to any of the following web browser in order to access the maximum functionality of this website. </p>
                    <ul class="browsers">
                        <li>
                            <a href="https://www.google.com/intl/en/chrome/browser/">
                                <img src="../img/browsers/chrome.png" alt="">
                                <div>Google Chrome</div>
                            </a>
                        </li>
                        <li>
                            <a href="http://www.mozilla.org/en-US/firefox/new/">
                                <img src="../img/browsers/firefox.png" alt="">
                                <div>Mozilla Firefox</div>
                            </a>
                        </li>
                        <li>
                            <a href="http://www.opera.com/computer/windows">
                                <img src="../img/browsers/opera.png" alt="">
                                <div>Opera</div>
                            </a>
                        </li>
                        <li>
                            <a href="http://safari.en.softonic.com/">
                                <img src="../img/browsers/safari.png" alt="">
                                <div>Safari</div>
                            </a>
                        </li>
                        <li>
                            <a href="http://windows.microsoft.com/en-us/internet-explorer/downloads/ie-10/worldwide-languages">
                                <img src="../img/browsers/ie.png" alt="">
                                <div>Internet Explorer(New)</div>
                            </a>
                        </li>
                    </ul>
                    <p>Upgrade your browser for a Safer and Faster web experience. <br/>Thank you for your patience...</p>
                </div>   
            <![endif]-->