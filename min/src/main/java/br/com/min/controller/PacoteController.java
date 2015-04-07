package br.com.min.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.min.entity.Link;
import br.com.min.entity.Pacote;
import br.com.min.entity.Usuario;
import br.com.min.service.PacoteService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/pacotes")
public class PacoteController {

	@Autowired
	private PacoteService pacoteService;
	
	@RequestMapping(value="/cliente/{clienteId}/ativos", method=RequestMethod.GET)
	public @ResponseBody List<Pacote> findAtivos(@PathVariable("clienteId") Long clienteId){
		List<Pacote> result = pacoteService.findPacotesAtivos(clienteId);
		for(Pacote pacote : result){
			pacote.addLink("descontar", "/pacotes/" + pacote.getId() + "/descontar", Link.TYPE_POST);
			if(pacote.getQuantidade() < pacote.getQuantidadeVendida()){
				pacote.addLink("visitas", "/pacotes/" + clienteId + "/visitas", Link.TYPE_GET);
			}
		}
		return result;
	}

	@RequestMapping(value="/cliente/{clienteId}/fechados", method=RequestMethod.GET)
	public @ResponseBody List<Pacote> findFechados(@PathVariable("clienteId") Long clienteId){
		List<Pacote> result = pacoteService.findPacotesAtivos(clienteId);
		for(Pacote pacote : result){
			pacote.addLink("visitas", "/pacotes/" + clienteId + "/visitas", Link.TYPE_GET);
		}
		return result;
	}
	
	@RequestMapping(value="/{id}/descontar/{funcionarioId}/{assistenteId}", method=RequestMethod.GET)
	public @ResponseBody Pacote descontar(@PathVariable Long id, @PathVariable("funcionarioId") Long funcionarioId, @PathVariable(value="assistenteId") Long assistenteId,  HttpServletRequest request){
		if(assistenteId < 1){
			assistenteId = null;
		}
		Pacote pacote = pacoteService.descontarDoPacote(id, funcionarioId, assistenteId, Utils.getUsuarioLogado(request).getPessoa());
		return pacote;
	}
	
}
