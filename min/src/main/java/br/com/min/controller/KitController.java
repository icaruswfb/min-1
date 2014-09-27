package br.com.min.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Kit;
import br.com.min.entity.Role;
import br.com.min.entity.Servico;
import br.com.min.service.KitService;
import br.com.min.service.ServicoService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/kits")
public class KitController {

	@Autowired
	private KitService service;
	@Autowired
	private ServicoService servicoService;
	
	@RequestMapping("/")
	public ModelAndView listar(HttpServletRequest request){
		return list(service.listar(), null, request);
	}

	private ModelAndView list(List<Kit> lista, String pesquisa, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("kits");
		if( Utils.hasRole(Role.ADMIN, request)){
			mv.addObject("kits", lista);
			mv.addObject("pesquisa", pesquisa);
		}
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@RequestBody() Kit kit, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			service.persist(kit);
		}
		return listar(request);
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(HttpServletRequest request){
		return editar(null, request);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("kit");
		if(Utils.hasRole(Role.ADMIN, request)){
			Kit kit;
			if(id == null){
				kit = new Kit();
			}else{
				kit = service.findById(id);
			}
			mv.addObject("kit", kit);
			List<Servico> servicos = servicoService.findServico(null);
			mv.addObject("servicos", servicos);
		}
		return mv;
	}
}
