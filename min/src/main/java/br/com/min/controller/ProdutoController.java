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

import br.com.min.controller.vo.EstoqueVO;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;
import br.com.min.entity.TipoLancamentoEstoque;
import br.com.min.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(service.listar(), null);
	}

	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public @ResponseBody() List<Produto> listarProdutos(){
		return service.listar();
	}
	
	private ModelAndView list(List<Produto> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("produtos");
		mv.addObject("produtos", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(){
		return editar(null);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("produto");
		
		Produto produto;
		if(id == null){
			produto = new Produto();
		}else{
			produto = service.findById(id);
		}
		mv.addObject("produto", produto);
		
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("servico") Produto produto){
		service.persist(produto);
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id){
		service.delete(id);
		return listar();
	}
	
	@RequestMapping(value="/listarEstoque/{id}", method=RequestMethod.GET)
	public @ResponseBody EstoqueVO listarEstoque(@PathVariable("id") Long id){
		Produto produto = service.findById(id);
		List<LancamentoEstoque> lancamentos = service.findLancamentosEstoquePorProduto(id);
		EstoqueVO vo = prepararVO(lancamentos, produto);
		return vo;
	}
	
	@RequestMapping(value="/addEstoque", method=RequestMethod.POST)
	public @ResponseBody String addEstoque(Long produtoId, Long quantidade, String tipo){
		TipoLancamentoEstoque tipoLancamento = TipoLancamentoEstoque.valueOf(tipo);
		service.addEstoque(produtoId, quantidade, tipoLancamento);
		return "ok";
	}
	
	private EstoqueVO prepararVO(List<LancamentoEstoque> lancamentos, Produto produto){
		EstoqueVO vo = new EstoqueVO();
		vo.setProduto(produto);
		Long quantidade = 0L;
		for(LancamentoEstoque lancamento : lancamentos){
			lancamento.setProduto(null);
			if(TipoLancamentoEstoque.ENTRADA.equals(lancamento.getTipo())){
				quantidade += lancamento.getQuantidade();
			}else if(TipoLancamentoEstoque.SAIDA.equals(lancamento.getTipo())){
				quantidade -= lancamento.getQuantidade();
			}
			if(lancamento.getComanda() != null){
				lancamento.getComanda().setEstoque(null);
				lancamento.getComanda().setPagamentos(null);
				lancamento.getComanda().setProdutos(null);
				lancamento.getComanda().setServicos(null);
			}
		}
		vo.setLancamentos(lancamentos);
		vo.setQuantidade(quantidade);
		return vo;
	}
	
}
