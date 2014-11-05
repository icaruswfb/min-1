package br.com.min.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Role;
import br.com.min.entity.Servico;
import br.com.min.entity.TipoServico;
import br.com.min.service.ServicoService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

	private static final long MINUTO_MILLIS = 60000L;
	@Autowired
	private ServicoService service;
	
	@RequestMapping("/")
	public ModelAndView listar(HttpServletRequest request){
		return list(service.listar(), null, request);
	}
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public @ResponseBody List<Servico> listarServicos(){
		return (service.listar());
	}
	
	private ModelAndView list(List<Servico> lista, String pesquisa, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("servicos");
		if( Utils.hasRole(Role.ADMIN, request)){
			mv.addObject("servicos", lista);
			mv.addObject("pesquisa", pesquisa);
		}
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(HttpServletRequest request){
		return editar(null, request);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("servico");
		if(Utils.hasRole(Role.ADMIN, request)){
			Servico servico;
			if(id == null){
				servico = new Servico();
			}else{
				servico = service.findById(id);
			}
			mv.addObject("tiposServico", service.listarTipoServico());
			mv.addObject("servico", servico);
		}
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("servico") Servico servico, Long tipoServicoId, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			if(servico.getDuracaoMinutos() != null){
				servico.setDuracao((servico.getDuracaoMinutos() * MINUTO_MILLIS));
			}
			if(servico.getTempoAcaoProdutoMinutos() != null){
				servico.setTempoAcaoProduto((servico.getTempoAcaoProdutoMinutos() * MINUTO_MILLIS ));
			}
			if(tipoServicoId != null){
				TipoServico tipoServico = service.findTipoServicoById(tipoServicoId);
				servico.setTipoServico(tipoServico);
			}
			service.persist(servico);
		}
		return listar(request);
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			service.delete(id);
		}
		return listar(request);
	}
	
}
