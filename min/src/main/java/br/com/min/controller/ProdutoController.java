package br.com.min.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.EstoqueVO;
import br.com.min.controller.vo.ProdutoPrecoVO;
import br.com.min.entity.CategoriaProduto;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;
import br.com.min.entity.Role;
import br.com.min.entity.TipoLancamentoEstoque;
import br.com.min.entity.UnidadeMedida;
import br.com.min.service.ProdutoService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping("/")
	public ModelAndView listar(HttpServletRequest request){
		return list(service.listar(), null, null, request);
	}

	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public @ResponseBody() List<Produto> listarProdutos(){
		return service.listar();
	}
	
	@RequestMapping(value="/pesquisar", method=RequestMethod.POST)
	public ModelAndView pesquisar(String pesquisa, CategoriaProduto categoriaProduto, HttpServletRequest request){
		List<Produto> lista = service.search(pesquisa, categoriaProduto);
		return list(lista, pesquisa, categoriaProduto, request);
	}
	
	private ModelAndView list(List<Produto> lista, String pesquisa, CategoriaProduto categoriaProduto, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("produtos");
		if(Utils.hasRole(Role.ADMIN, request)){
			mv.addObject("produtos", lista);
			mv.addObject("pesquisa", pesquisa);
			mv.addObject("categoria", categoriaProduto);
		}
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(HttpServletRequest request){
		return editar(null, request);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("produto");
		if(Utils.hasRole(Role.ADMIN, request)){
			Produto produto;
			if(id == null){
				produto = new Produto();
			}else{
				produto = service.findById(id);
			}
			mv.addObject("produto", produto);
		}
		
		
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("servico") Produto produto, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			if( produto.getUnidade().equals(UnidadeMedida.un) ){
				produto.setQuantidadePorUnidade(null);
			}
			produto = service.persist(produto);
			service.atualizarSituacaoEstoque(produto);;
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
	
	@RequestMapping(value="/precos")
	public ModelAndView listPrecos(String pesquisa, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("produtosPrecos");
		List<Produto> produtos = service.listar();
		List<ProdutoPrecoVO> vos = createPrecoVOs(produtos, null);
		mv.addObject("produtos", vos);
		return mv;
	}
	

	@RequestMapping(value="/precos/aplicar", method=RequestMethod.POST)
	public ModelAndView aplicarAlteracao(Double percentual, String pesquisa, CategoriaProduto categoriaProduto, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			List<Produto> produtos = service.search(pesquisa, categoriaProduto);
			for(Produto produto : produtos){
				Double precoNovo = produto.getCustoUnitario() + (produto.getCustoUnitario() * (percentual / 100));
				produto.setPrecoRevenda(precoNovo);
				service.persist(produto);
			}
		}
		return simularAlteracao(percentual, pesquisa, categoriaProduto, request);
	}
	
	@RequestMapping(value="/precos/simular", method=RequestMethod.POST)
	public ModelAndView simularAlteracao(Double percentual, String pesquisa, CategoriaProduto categoriaProduto, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("produtosPrecos");
		if(Utils.hasRole(Role.ADMIN, request)){
			List<Produto> produtos = service.search(pesquisa, categoriaProduto);
			mv.addObject("pesquisa", pesquisa);
			mv.addObject("categoria", categoriaProduto);
			List<ProdutoPrecoVO> vos = createPrecoVOs(produtos, percentual);
			mv.addObject("percentual", percentual);
			mv.addObject("produtos", vos);
		}
		return mv;
	}
	
	@RequestMapping(value="/precos/pesquisar", method=RequestMethod.POST)
	public ModelAndView pesquisarPrecos(String pesquisa, CategoriaProduto categoriaProduto, HttpServletRequest request){
		return simularAlteracao(null, pesquisa, categoriaProduto, request);
	}
	
	private List<ProdutoPrecoVO> createPrecoVOs(List<Produto> produtos, Double percentualNovo){
		List<ProdutoPrecoVO> vos = new ArrayList<ProdutoPrecoVO>();
		for(Produto produto : produtos){
			ProdutoPrecoVO vo = new ProdutoPrecoVO();
			vo.setProduto(produto);
			if(produto.getPrecoRevenda() != null && produto.getCustoUnitario() != null){
				Double percentual = produto.getPrecoRevenda() / produto.getCustoUnitario() * 100 - 100;
				vo.setPercentual(percentual);
				if(percentualNovo != null){
					Double precoNovo = produto.getCustoUnitario() + (produto.getCustoUnitario() * (percentualNovo / 100));
					vo.setPrecoNovo(precoNovo);
					vo.setDiferenca(percentualNovo - percentual);
				}
			}
			vos.add(vo);
		}
		return vos;
	}
	
	@RequestMapping(value="/listarEstoque/{id}", method=RequestMethod.GET)
	public @ResponseBody EstoqueVO listarEstoque(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			Produto produto = service.findById(id);
			List<LancamentoEstoque> lancamentos = service.findLancamentosEstoquePorProduto(id);
			EstoqueVO vo = prepararVO(lancamentos, produto);
			return vo;
		}
		return null;
	}
	
	@RequestMapping(value="/addEstoque", method=RequestMethod.POST)
	public @ResponseBody String addEstoque(Long produtoId, Long quantidade, String tipo, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			TipoLancamentoEstoque tipoLancamento = TipoLancamentoEstoque.valueOf(tipo);
			service.addEstoque(produtoId, quantidade, tipoLancamento);
		}
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
		}
		vo.setLancamentos(lancamentos);
		vo.setQuantidade(quantidade);
		return vo;
	}
	
}
