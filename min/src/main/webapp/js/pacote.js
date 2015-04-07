Pacote = {
		renderPacotesAbertos: function(){
			var clienteId = $("#cliente-id").val();
			$.ajax({
				url: "/min/web/pacotes/cliente/" + clienteId + "/ativos",
				type: 'GET',
				success: function(data){
					var html = "";
					$.each(data, function(index, pacote){
						html += "<div class='pacote'>";
						html += "<div>";
						html += "Foram feitas " + pacote.quantidade + " vezes do total de " + pacote.quantidadeVendida + " do pacote de " + pacote.servico.nome;
						html += "</div>";
						$.each(pacote.links, function(i, link){
							html += "<div>";
							html += "<a href='"+link.href+"' >"
							html += link.rel;
							html += "</a>";
							html += "</div>";
						});
						html += "</div>";
					});
					$(".pacotes").html(html);
				}
			});
			
		}
}