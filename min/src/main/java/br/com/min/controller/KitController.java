package br.com.min.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.KitVO;
import br.com.min.controller.vo.ProdutoKitVO;
import br.com.min.controller.vo.ServicoKitVO;
import br.com.min.entity.Kit;
import br.com.min.entity.Produto;
import br.com.min.entity.ProdutoQuantidade;
import br.com.min.entity.Role;
import br.com.min.entity.Servico;
import br.com.min.service.KitService;
import br.com.min.service.ProdutoService;
import br.com.min.service.ServicoService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/kits")
public class KitController {

	@Autowired
	private KitService service;
	@Autowired
	private ServicoService servicoService;
	@Autowired
	private ProdutoService produtoService;
	
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
	public @ResponseBody String salvar(@RequestBody() KitVO kit, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			Kit entity = converterKit(kit);
			service.persist(entity);
		}
		return "OK";
	}
	
	private Kit converterKit(KitVO vo){
		Kit kit = null;
		if(vo.getId() != null){
			kit = service.findById(vo.getId());
			kit.getServicos().clear();
			kit.getProdutos().clear();
		}else{
			kit = new Kit();
		}
		for(ServicoKitVO servicoVO : vo.getServicos()){
			Servico servico = servicoService.findById(servicoVO.getId());
			kit.getServicos().add(servico);
		}
		for(ProdutoKitVO produtoVO : vo.getProdutos()){
			Produto produto = produtoService.findById(produtoVO.getId());
			ProdutoQuantidade produtoQuantidade = new ProdutoQuantidade();
			produtoQuantidade.setProduto(produto);
			produtoQuantidade.setQuantidade(produtoVO.getQuantidade());
			kit.getProdutos().add(produtoQuantidade);
		}
		kit.setNome(vo.getNome());
		kit.setValor(vo.getValor());
		return kit;
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
				List<Servico> servicos = servicoService.findServico(null);
				mv.addObject("servicos", servicos);
				List<Produto> produtos = produtoService.find(null);
				mv.addObject("produtos", produtos);
			}
			mv.addObject("kit", kit);
		}
		return mv;
	}
	

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			service.delete(id);
		}
		return listar(request);
	}
	
	
}
