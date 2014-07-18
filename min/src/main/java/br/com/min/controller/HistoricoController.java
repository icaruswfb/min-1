package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.min.entity.Historico;
import br.com.min.service.HistoricoService;

@Controller("")
@RequestMapping("/historico")
public class HistoricoController {
	
	@Autowired
	private HistoricoService historicoService;
	
	@RequestMapping(value="/cliente/{id}", method=RequestMethod.GET)
	public @ResponseBody() List<Historico> buscarPorCliente(@PathVariable("id")Long id){
		return historicoService.findByClienteId(id);
	}
	
}
