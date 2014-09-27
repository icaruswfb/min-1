package br.com.min.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.MessageVO;
import br.com.min.controller.vo.VerificacaoVO;
import br.com.min.entity.Comanda;
import br.com.min.entity.FluxoPagamento;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.Historico;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Produto;
import br.com.min.entity.Role;
import br.com.min.entity.Servico;
import br.com.min.service.ComandaService;
import br.com.min.service.HistoricoService;
import br.com.min.service.PessoaService;
import br.com.min.service.ProdutoService;
import br.com.min.service.ServicoService;
import br.com.min.utils.Utils;

@Controller("")
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private HistoricoService historicoService;
	@Autowired
	private ComandaService comandaService;
	@Autowired
	private ServicoService servicoService;
	@Autowired
	private ProdutoService produtoService;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(pessoaService.listarClientes(), null);
	}
	
	private ModelAndView list(List<Pessoa> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("clientes");
		mv.addObject("clientes", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(HttpServletRequest request){
		return editar(null, request);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("cliente");
		Pessoa cliente;
		if(id == null){
			cliente = new Pessoa();
		}else{
			cliente = pessoaService.findById(id);
			mv.addObject("formasPagamento", FormaPagamento.values());
		}
		mv.addObject("cliente", cliente);
		
		return mv;
	}
	
	@RequestMapping(value="/historico/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Historico> listarHistorico(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			List<Historico> historico = historicoService.findByClienteId(id);
			limparHistoricoJSON(historico);
			return historico;
		}
		throw new RuntimeException("Sem permissão para acessar histórico de cliente");
	}
	
	private void limparHistoricoJSON(List<Historico> historico){
		for(Historico h : historico){
			if(h.getCriador() != null){
				h.getCriador().setUsuario(null);
			}
			if(h.getFuncionario() != null){
				h.getFuncionario().setUsuario(null);
			}
		}
	}
	
	@RequestMapping(value="/pagar", method=RequestMethod.POST)
	public @ResponseBody Comanda pagar(Long comandaId, String formaPagamento, Double valor, Integer parcelas, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request) || Utils.hasRole(Role.CAIXA, request)){
			Comanda comanda = comandaService.findById(comandaId);
			if(valor > 0){
				Pagamento pagamento = new Pagamento();
				pagamento.setComanda(comanda);
				pagamento.setData(new Date());
				pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
				pagamento.setFluxoPagamento(FluxoPagamento.ENTRADA);
				
				if(pagamento.getFormaPagamento().equals(FormaPagamento.Credito)){
					Double credito = comandaService.getCreditoCliente(comanda.getCliente().getId());
					if(valor > credito){
						valor = credito;
					}
				}
				if(pagamento.getFormaPagamento().isParcelavel()){
					if(parcelas == null || parcelas <= 0){
						parcelas = 1;
					}else{
						Double valorParcela = valor / parcelas;
						Calendar dataPagamento = Calendar.getInstance();
						dataPagamento.setTime(new Date());
						for(int i = 1; i <= parcelas; i++){
							Pagamento parcelado = new Pagamento();
							parcelado.setFluxoPagamento(FluxoPagamento.ENTRADA);
							parcelado.setComanda(comanda);
							dataPagamento.add(Calendar.MONTH, 1);
							parcelado.setData(dataPagamento.getTime());
							parcelado.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
							parcelado.setParcelamento(parcelas);
							parcelado.setParcela(i);
							parcelado.setValor(valorParcela);
							comanda.getPagamentos().add(parcelado);
						}
					}
				}else{
					pagamento.setValor(valor);
					comanda.getPagamentos().add(pagamento);
				}
				comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
			}
			limparComandaJSON(comanda);
			return comanda;
		}
		throw new RuntimeException("Sem permissão para pagar a comanda");
	}

	@RequestMapping(value="/deletarPagamento/{id}/{clienteId}", method=RequestMethod.GET)
	public @ResponseBody Comanda deletarPagamento(@PathVariable("id") Long id, @PathVariable("clienteId") Long clienteId, HttpServletRequest request){
		if(Utils.hasRole(Role.CAIXA, request)){
			Comanda comanda = comandaService.findComandaAberta(clienteId);
			Pagamento pagamento = new Pagamento();
			pagamento.setId(id);
			comanda.getPagamentos().remove(pagamento);
			comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
			limparComandaJSON(comanda);
			return comanda;
		}
		throw new RuntimeException("Sem permissão para deletar o pagamento da comanda");
	}
	
	
	private void limarPagamentoJSON(Pagamento pagamento){
		pagamento.setComanda(null);
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("cliente") Pessoa cliente, HttpServletRequest request){
		if(Utils.hasRole(Role.CAIXA, request)){
			cliente.setFuncionario(false);
			pessoaService.persist(cliente);
		}
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.CAIXA, request)){
			pessoaService.delete(id);
		}
		return listar();
	}
	
	@RequestMapping(value="/deleteComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody String deletarComanda(@PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			comandaService.delete(id, Utils.getUsuarioLogado(request).getPessoa());
			return "OK";
		}
		return "NOT";
	}
	
	@RequestMapping(value="/findComandas/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Comanda> findComandas(@PathVariable("id") Long id, HttpServletRequest request){
		Comanda comanda = new Comanda();
		Pessoa cliente = pessoaService.findById(id);
		comanda.setCliente(cliente);
		List<Comanda> comandas = comandaService.find(comanda);
		limparComandasJSON(comandas);
		return comandas;
	}

	@RequestMapping(value="/findComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda findComanda(@PathVariable("id") Long id, HttpServletRequest request){
		Comanda comanda = comandaService.findById(id);
		comanda = limparComandaJSON(comanda);
		return comanda;
	}
	
	
	@RequestMapping(value="/findComandasFechadas/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Comanda> findComandasFechadas(@PathVariable("id") Long id, HttpServletRequest request){
		Comanda comanda = new Comanda();
		Pessoa cliente = pessoaService.findById(id);
		comanda.setCliente(cliente);
		List<Comanda> comandas = comandaService.find(comanda, true);
		limparComandasSimplificadaJSON(comandas);
		return comandas;
	}
	
	@RequestMapping(value="/findComandaAberta/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda findComandaAberta(@PathVariable("id") Long id){
		return limparComandaJSON(comandaService.findComandaAberta(id));
	}
	
	
	@RequestMapping(value="/abrirComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda abrirComanda(@PathVariable("id") Long id, HttpServletRequest request){
		Comanda comanda = comandaService.findComandaAberta(id);
		if(comanda == null){
			comanda = new Comanda();
			Pessoa cliente = pessoaService.findById(id);
			comanda.setCliente(cliente);
			comanda.setAbertura(new Date());
			comanda.setCredito(comandaService.getCreditoCliente(id));
			
			comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
		}
		limparComandaJSON(comanda);
		return comanda;
	}
	
	@RequestMapping(value="/salvarComanda", method=RequestMethod.POST)
	public @ResponseBody Comanda salvarComanda(HttpServletRequest request){
		Comanda comanda = criarComanda(request);
//		comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
		limparComandaJSON(comanda);
		return comanda;
	}
	
	@RequestMapping(value="/verificarComanda", method=RequestMethod.POST)
	public @ResponseBody VerificacaoVO verificarCamanda(HttpServletRequest request){
		Comanda comanda = criarComanda(request);
		comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
		
		return verificarComanda(comanda);
	}
	
	private VerificacaoVO verificarComanda(Comanda comanda){
		VerificacaoVO vo = new VerificacaoVO();
		if(comanda.getValorCobrado() > comanda.getValorPago()){
			vo.addMessage(MessageVO.WARNING, "O valor cobrado é maior que o valor pago, fechar a comanda agora irá gerar um débito.");
		}
		if(comanda.getValorCobrado() < comanda.getValorPago()){
			vo.addMessage(MessageVO.WARNING, "O valor cobrado é menor que o valor pago, fechar a comanda agora irá gerar um crédito.");
		}
		for(LancamentoServico lancamentoServico : comanda.getServicos()){
			boolean hasServicoError = false;
			boolean hasFuncionarioError = false;
			boolean hasProdutoError = false;
			if( ! hasServicoError && lancamentoServico.getServico() == null){
				hasServicoError = true;
				vo.setCriticalError(true);
				vo.addMessage(MessageVO.ERROR, "Todos os lançamentos de serviço devem ter um serviço vinculado.");
			}
			if( ! hasFuncionarioError && lancamentoServico.getFuncionario() == null){
				hasFuncionarioError = true;
				vo.setCriticalError(true);
				vo.addMessage(MessageVO.ERROR, "Todos os lançamentos de serviço devem ter um funcionário vinculado.");
			}
			if( ! hasProdutoError ){
				for(LancamentoProduto lancamentoProduto : lancamentoServico.getProdutosUtilizados()){
					if(lancamentoProduto.getProduto() == null){
						hasProdutoError = true;
						vo.setCriticalError(true);
						vo.addMessage(MessageVO.ERROR, "Todos os lançamentos de produto do serviço devem ter um produto vinculado.");
						break;
					}
				}
			}
		}
		for(LancamentoProduto lancamentoProduto : comanda.getProdutos()){
			boolean hasProdutoError = false;
			boolean hasVendedorError = false;
			if( ! hasProdutoError && lancamentoProduto.getProduto() == null){
				hasProdutoError = true;
				vo.setCriticalError(true);
				vo.addMessage(MessageVO.ERROR, "Todos os lançamentos de produto devem ter um produto vinculado.");
			}
			if( ! hasVendedorError && lancamentoProduto.getVendedor() == null){
				hasVendedorError = true;
				vo.setCriticalError(true);
				vo.addMessage(MessageVO.ERROR, "Todos os lançamentos de produto devem ter um vendedor vinculado.");
			}
		}
		
		if(vo.getMessages().isEmpty()){
			vo.setSuccess(true);
			vo.addMessage(MessageVO.SUCCESS, "A comanda está pronta para ser fechada.");
		}
		vo.setUltimaAtualizacao(comanda.getUltimaAtualizacao());
		return vo;
	}
	
	@RequestMapping(value="/fecharComanda", method=RequestMethod.POST)
	public @ResponseBody Comanda fecharComanda(HttpServletRequest request){
		if( Utils.hasRole(Role.ADMIN, request) || Utils.hasRole(Role.CAIXA, request)){
			Comanda comanda = criarComanda(request);
			comanda = comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
			VerificacaoVO vo = verificarComanda(comanda);
			if( ! vo.isCriticalError()){
				comanda.setFechamento(new Date());
				comanda = comandaService.persist(comanda, true, Utils.getUsuarioLogado(request).getPessoa());
				limparComandaJSON(comanda);
				return comanda;
			}
			throw new RuntimeException("Há erros críticos com a comanda que está sendo fechada");
		}
		throw new RuntimeException("Sem permissão para fechar a comanda");
	}
	
	@RequestMapping(value="/fecharComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody VerificacaoVO fecharComanda(@PathVariable("id") Long id, HttpServletRequest request){
		if( Utils.hasRole(Role.ADMIN, request) || Utils.hasRole(Role.CAIXA, request)){
			Comanda comanda = comandaService.findById(id);
			VerificacaoVO vo = verificarComanda(comanda);
			if( ! vo.isCriticalError()){
				comanda.setFechamento(new Date());
				comandaService.persist(comanda, true, Utils.getUsuarioLogado(request).getPessoa());
			}
			return vo;
		}
		throw new RuntimeException("Sem permissão para fechar a comanda");
	}
	
	private Comanda criarComanda(HttpServletRequest request){
		Long comandaId = Long.parseLong(request.getParameter("comandaId"));
		Comanda comanda = comandaService.findById(comandaId);
		if(comanda.getFechamento() != null){
			throw new RuntimeException("Alteração de comanda já fechada");
		}
		return comanda;
		/*
		Long ultimaAtualizacao = Long.parseLong(request.getParameter("ultimaAtualizacao"));
		Long diferencaTempo = ultimaAtualizacao - comanda.getUltimaAtualizacao();
		if(diferencaTempo > 1000 || diferencaTempo < -1000){
			throw new RuntimeException("Comanda desatualizada. Atualize antes de fazer as alterações.");
		}

		Collection<LancamentoServico> servicos = criarServicos(request, comanda);
		Collection<LancamentoProduto> produtos = criarProdutos(request, comanda);
		
		if(Utils.hasRole(Role.ADMIN, request)){
			Double descontos = Double.parseDouble(request.getParameter("descontos"));
			comanda.setDesconto(descontos);
		}else{
			//verificarPermissaoAlteracaoComanda(comanda, produtos, servicos);
		}
		
		comanda.getServicos().clear();
		comanda.getProdutos().clear();
		comanda.getServicos().addAll(servicos);
		comanda.getProdutos().addAll(produtos);
		return comanda;
		 */
	}
	
	@RequestMapping(value="/ultimaAtualizacao/{comandaId}", method=RequestMethod.GET)
	public @ResponseBody Long findUltimaAtualizacao(@PathVariable("comandaId") Long comandaId){
		return comandaService.findUltimaAtualizacao(comandaId);
	}
	
	@RequestMapping(value="/addDesconto", method=RequestMethod.POST)
	public @ResponseBody Comanda addDesconto(@RequestParam(required=true) Long clienteId, 
																					@RequestParam(required=true) Double desconto, HttpServletRequest request){
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		if(Utils.hasRole(Role.ADMIN, request)){
			comanda.setDesconto(desconto);
			comandaService.persist(comanda, Utils.getUsuarioLogado(request).getPessoa());
		}
		return limparComandaJSON(comanda);
	}
	
	@RequestMapping(value="/addServico", method=RequestMethod.POST)
	public @ResponseBody Comanda addServico(@RequestParam(required=true) Long servicoId, 
																				@RequestParam(required=true)Long funcionarioId, 
																				@RequestParam(required=false)Long assistenteId, 
																				@RequestParam(required=true)Long clienteId, HttpServletRequest request){
		LancamentoServico lancamentoServico = new LancamentoServico();
		Pessoa funcionario = pessoaService.findById(funcionarioId);
		Pessoa assistente = null;
		if(assistenteId != null){
			assistente = pessoaService.findById(assistenteId);
		}
		Servico servico = servicoService.findById(servicoId);
		
		lancamentoServico.setAssistente(assistente);
		lancamentoServico.setDataCriacao(new Date());
		lancamentoServico.setFuncionario(funcionario);
		lancamentoServico.setServico(servico);
		lancamentoServico.setValor(servico.getPreco());
		
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		lancamentoServico.setComanda(comanda);
		
		comanda = comandaService.addServico(lancamentoServico, comanda, Utils.getUsuarioLogado(request).getPessoa());
		return limparComandaJSON(comanda);
	}


	@RequestMapping(value="/addProduto", method=RequestMethod.POST)
	public @ResponseBody Comanda addProduto(@RequestParam(required=true) Long produtoId, 
																				@RequestParam(required=true)Long vendedorId, 
																				@RequestParam(required=true)Long quantidade, 
																				@RequestParam(required=true)Long clienteId, HttpServletRequest request){
		LancamentoProduto lancamentoProduto = new LancamentoProduto();
		Pessoa vendedor = pessoaService.findById(vendedorId);
		Produto produto = produtoService.findById(produtoId);
		
		lancamentoProduto.setQuantidadeUtilizada(quantidade);
		lancamentoProduto.setDataCriacao(new Date());
		lancamentoProduto.setProduto(produto);
		lancamentoProduto.setValor(produto.getPrecoRevenda() * quantidade);
		lancamentoProduto.setVendedor(vendedor);
		
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		lancamentoProduto.setComanda(comanda);
		
		comanda = comandaService.addProduto(lancamentoProduto, comanda, Utils.getUsuarioLogado(request).getPessoa());
		
		return limparComandaJSON(comanda);
	}
	
	@RequestMapping(value="/addProdutoServico", method=RequestMethod.POST)
	public @ResponseBody Comanda addProdutoServico(@RequestParam(required=true) Long produtoId, 
																				@RequestParam(required=true)Long servicoId, 
																				@RequestParam(required=true)Long quantidade, 
																				@RequestParam(required=true)Long clienteId, HttpServletRequest request){
		LancamentoProduto lancamentoProduto = new LancamentoProduto();
		Produto produto = produtoService.findById(produtoId);
		
		lancamentoProduto.setQuantidadeUtilizada(quantidade);
		lancamentoProduto.setDataCriacao(new Date());
		lancamentoProduto.setProduto(produto);
		lancamentoProduto.setValor(produto.getPrecoRevenda() * quantidade);
		
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		for(LancamentoServico servico : comanda.getServicos()){
			if(servico.getId().equals(servicoId)){
				servico.getProdutosUtilizados().add(lancamentoProduto);
				break;
			}
		}
		comanda = comandaService.addProdutoServico(lancamentoProduto, comanda, Utils.getUsuarioLogado(request).getPessoa());
		
		return limparComandaJSON(comanda);
	}
	
	@RequestMapping(value="/deleteServico/{clienteId}/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda deleteServico(@PathVariable("clienteId") Long clienteId, @PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			comandaService.deleteLancamentoServico(id, Utils.getUsuarioLogado(request).getPessoa());
		}
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		return limparComandaJSON(comanda);
	}

	@RequestMapping(value="/deleteProduto/{clienteId}/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda deleteProduto(@PathVariable("clienteId") Long clienteId, @PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			comandaService.deleteLancamentoProduto(id, Utils.getUsuarioLogado(request).getPessoa());
		}
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		return limparComandaJSON(comanda);
	}

	@RequestMapping(value="/deleteProdutoServico/{clienteId}/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda deleteProdutoServico(@PathVariable("clienteId") Long clienteId, @PathVariable("id") Long id, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			comandaService.deleteLancamentoProdutoServico(id, Utils.getUsuarioLogado(request).getPessoa());
		}
		Comanda comanda = comandaService.findComandaAberta(clienteId);
		return limparComandaJSON(comanda);
	}
	
	private List<Comanda> limparComandasJSON(List<Comanda> comandas){
		for(Comanda comanda : comandas){
			limparComandaJSON(comanda);
		}
		return comandas;
	}
	private List<Comanda> limparComandasSimplificadaJSON(List<Comanda> comandas){
		for(Comanda comanda : comandas){
			limparComandaSimplificadaJSON(comanda);
		}
		return comandas;
	}
	private Comanda limparComandaSimplificadaJSON(Comanda comanda){
		if(comanda != null){
			comanda.setProdutos(null);
			comanda.setServicos(null);
			comanda.setEstoque(null);
			comanda.setCliente(null);
			comanda.setCriador(null);
			comanda.setPagamentos(null);
			comanda.setComissoes(null);
		}
		return comanda;
	}
	
	private Comanda limparComandaJSON(Comanda comanda){
		if(comanda != null){
			for(LancamentoProduto produto : comanda.getProdutos()){
				produto.setComanda(null);
				if(produto.getVendedor() != null){
					produto.getVendedor().setUsuario(null);
				}
			}
			for(LancamentoServico servico : comanda.getServicos()){
				servico.setComanda(null);
				if(servico.getFuncionario() != null){
					servico.getFuncionario().setUsuario(null);
				}
				if(servico.getAssistente() != null){
					servico.getAssistente().setUsuario(null);
				}
			}
			comanda.setEstoque(null);
			for(Pagamento pagamento : comanda.getPagamentos()){
				limarPagamentoJSON(pagamento);
			}
			comanda.setComissoes(null);
		}
		return comanda;
	}
	
}
