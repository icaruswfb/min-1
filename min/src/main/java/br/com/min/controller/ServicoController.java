package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Servico;
import br.com.min.service.ServicoService;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

	@Autowired
	private ServicoService service;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(service.listar(), null);
	}
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public @ResponseBody List<Servico> listarServicos(){
		return (service.listar());
	}
	
	private ModelAndView list(List<Servico> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("servicos");
		mv.addObject("servicos", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(){
		return editar(null);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("servico");
		
		Servico servio;
		if(id == null){
			servio = new Servico();
		}else{
			servio = service.findById(id);
		}
		mv.addObject("servico", servio);
		
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("servico") Servico servico){
		if(servico.getDuracaoMinutos() != null){
			servico.setDuracao((servico.getDuracaoMinutos() * 60 * 1000L));
		}
		service.persist(servico);
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id){
		service.delete(id);
		return listar();
	}
	
}
