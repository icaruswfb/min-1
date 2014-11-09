package br.com.min.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.ProjecaoEstoqueVO;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;
import br.com.min.entity.Role;
import br.com.min.entity.SituacaoEstoque;
import br.com.min.entity.TipoLancamentoEstoque;
import br.com.min.entity.UnidadeMedida;
import br.com.min.service.ProdutoService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

	@Autowired
	private ProdutoService produtoService;
	
	@RequestMapping("/")
	public ModelAndView listarProjecao(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("projecaoCompraEstoque");
		if(Utils.hasRole(Role.ADMIN, request)){
			Produto query = new Produto();
			query.setSituacaoEstoque(SituacaoEstoque.ALERTA);
			List<Produto> alertas = produtoService.find(query);
			query.setSituacaoEstoque(SituacaoEstoque.CRITICA);
			List<Produto> criticos = produtoService.find(query);
			query.setSituacaoEstoque(SituacaoEstoque.BOA);
			List<Produto> boas = produtoService.find(query);
			
			List<ProjecaoEstoqueVO> alertasVO = criarProjecao(alertas);
			List<ProjecaoEstoqueVO> criticosVO = criarProjecao(criticos);
			List<ProjecaoEstoqueVO> boasVO = criarProjecao(boas);
			
			mv.addObject("alertas", alertasVO);
			mv.addObject("criticos", criticosVO);
			mv.addObject("boas", boasVO);
		}
		
		return mv;
	}
	
	private List<ProjecaoEstoqueVO> criarProjecao(List<Produto> produtos) {
		List<ProjecaoEstoqueVO> result = new ArrayList<>();
		for(Produto produto : produtos){
			ProjecaoEstoqueVO vo = new ProjecaoEstoqueVO();
			vo.setProduto(produto);
			List<LancamentoEstoque> lancamentos = produtoService.findLancamentosEstoquePorProduto(produto.getId());
			Long quantidade = 0L;
			Long consumidoPerido = 0L;
			Double projecaoMensal = 0.0;
			Calendar ultimoMes = Calendar.getInstance();
			Calendar penultimoMes = Calendar.getInstance();
			Calendar antepenultimoMes = Calendar.getInstance();
			//Quantidade de meses a serem utilizados para a média ponderada
			ultimoMes.add(Calendar.MONTH, -1);
			penultimoMes.add(Calendar.MONTH, -2);
			antepenultimoMes.add(Calendar.MONTH, -3);
			for(LancamentoEstoque lancamento : lancamentos){
				if(TipoLancamentoEstoque.ENTRADA.equals(lancamento.getTipo())){
					quantidade += lancamento.getQuantidade();
				}else if(TipoLancamentoEstoque.SAIDA.equals(lancamento.getTipo())){
					if(lancamento.getDataCriacao().after(ultimoMes.getTime())){
						consumidoPerido += lancamento.getQuantidade();
						projecaoMensal += lancamento.getQuantidade() * 0.7;
					} else if(lancamento.getDataCriacao().after(penultimoMes.getTime())){
						consumidoPerido += lancamento.getQuantidade();
						projecaoMensal += lancamento.getQuantidade() * 0.2;
					} else if(lancamento.getDataCriacao().after(antepenultimoMes.getTime())){
						consumidoPerido += lancamento.getQuantidade();
						projecaoMensal += lancamento.getQuantidade() * 0.1;
					}
					quantidade -= lancamento.getQuantidade();
				}
			}
			vo.setEstoqueAtual(quantidade);
			if( ! produto.getUnidade().equals(UnidadeMedida.un) ){
				if(produto.getQuantidadePorUnidade() != null){
					Double quantidadeUnidades = quantidade.doubleValue() / produto.getQuantidadePorUnidade().doubleValue();
					vo.setEstoqueAtualUnidade(quantidadeUnidades);
				}
			}
			vo.setConsumidoPeriodo(consumidoPerido);
			vo.setProjecaoCompraMensal(projecaoMensal);
			
			result.add(vo);
		}
		return result;
	}
	
}
