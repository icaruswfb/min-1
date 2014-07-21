package br.com.min.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Comanda;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.Historico;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Produto;
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
	
	private Map<Long, Pessoa> pessoas = new HashMap<>();
	private Map<Long, Servico> servicos = new HashMap<>();
	private Map<Long, Produto> produtos = new HashMap<>();
	
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
	public ModelAndView novo(){
		return editar(null);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("cliente");
		
		Pessoa cliente;
		if(id == null){
			cliente = new Pessoa();
		}else{
			cliente = pessoaService.findById(id);
		}
		mv.addObject("cliente", cliente);
		
		
		return mv;
	}
	
	@RequestMapping(value="/historico/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Historico> listarHistorico(@PathVariable("id") Long id){
		return historicoService.findByClienteId(id);
	}
	
	@RequestMapping(value="/pagar", method=RequestMethod.POST)
	public @ResponseBody Comanda pagar(Long comandaId, String formaPagamento, Double valor){
		Pagamento pagamento = new Pagamento();
		Comanda comanda = comandaService.findById(comandaId);
		pagamento.setComanda(comanda);
		pagamento.setData(new Date());
		pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
		if(pagamento.getFormaPagamento().equals(FormaPagamento.Crédito)){
			Double credito = comandaService.getCreditoCliente(comanda.getCliente().getId());
			if(valor > credito){
				valor = credito;
			}
		}
		pagamento.setValor(valor);
		comanda.getPagamentos().add(pagamento);
		comanda = comandaService.persist(comanda);
		limparComandaJSON(comanda);
		return comanda;
	}

	@RequestMapping(value="/deletarPagamento/{id}/{comandaId}", method=RequestMethod.GET)
	public @ResponseBody Comanda deletarPagamento(@PathVariable("id") Long id, @PathVariable("comandaId") Long comandaId){
		Comanda comanda = comandaService.findById(comandaId);
		Pagamento pagamento = new Pagamento();
		pagamento.setId(id);
		comanda.getPagamentos().remove(pagamento);
		comanda = comandaService.persist(comanda);
		limparComandaJSON(comanda);
		return comanda;
	}
	
	
	private void limarPagamentoJSON(Pagamento pagamento){
		pagamento.setComanda(null);
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("cliente") Pessoa cliente){
		cliente.setFuncionario(false);
		pessoaService.persist(cliente);
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id){
		pessoaService.delete(id);
		return listar();
	}
	
	@RequestMapping(value="/findComandas/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Comanda> findComandas(@PathVariable("id") Long id){
		Comanda comanda = new Comanda();
		Pessoa cliente = pessoaService.findById(id);
		comanda.setCliente(cliente);
		List<Comanda> comandas = comandaService.find(comanda);
		limparComandasJSON(comandas);
		return comandas;
	}
	
	@RequestMapping(value="/findComandasFechadas/{id}", method=RequestMethod.GET)
	public @ResponseBody List<Comanda> findComandasFechadas(@PathVariable("id") Long id){
		Comanda comanda = new Comanda();
		Pessoa cliente = pessoaService.findById(id);
		comanda.setCliente(cliente);
		List<Comanda> comandas = comandaService.find(comanda, true);
		limparComandasJSON(comandas);
		return comandas;
	}
	
	@RequestMapping(value="/findComandaAberta/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda findComandaAberta(@PathVariable("id") Long id){
		return limparComandaJSON(comandaService.findComandaAberta(id));
	}
	
	
	@RequestMapping(value="/abrirComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody Comanda abrirComanda(@PathVariable("id") Long id){
		Comanda comanda = new Comanda();
		Pessoa cliente = pessoaService.findById(id);
		comanda.setCliente(cliente);
		comanda.setAbertura(new Date());
		comanda.setCredito(comandaService.getCreditoCliente(id));
		
		comanda = comandaService.persist(comanda);
		limparComandaJSON(comanda);
		return comanda;
	}
	
	@RequestMapping(value="/salvarComanda", method=RequestMethod.POST)
	public @ResponseBody Comanda salvarComanda(HttpServletRequest request){
		Comanda comanda = criarComanda(request);
		comanda = comandaService.persist(comanda);
		limparComandaJSON(comanda);
		return comanda;
	}
	
	@RequestMapping(value="/fecharComanda", method=RequestMethod.POST)
	public @ResponseBody Comanda fecharComanda(HttpServletRequest request){
		Comanda comanda = criarComanda(request);
		comanda.setFechamento(new Date());
		comanda = comandaService.persist(comanda, true);
		limparComandaJSON(comanda);
		return comanda;
	}
	
	@RequestMapping(value="/fecharComanda/{id}", method=RequestMethod.GET)
	public @ResponseBody String fecharComanda(@PathVariable("id") Long id){
		Comanda comanda = comandaService.findById(id);
		comanda.setFechamento(new Date());
		comandaService.persist(comanda, true);
		return "ok";
	}
	
	private Comanda criarComanda(HttpServletRequest request){
		Long comandaId = Long.parseLong(request.getParameter("comandaId"));
		Double descontos = Double.parseDouble(request.getParameter("descontos"));
		Comanda comanda = comandaService.findById(comandaId);
		comanda.setDesconto(descontos);
		comanda.getServicos().clear();
		comanda.getProdutos().clear();
		Collection<LancamentoServico> servicos = criarServicos(request, comanda);
		comanda.getServicos().addAll(servicos);
		Collection<LancamentoProduto> produtos = criarProdutos(request, comanda);
		comanda.getProdutos().addAll(produtos);
		
		return comanda;
	}
	
	private Collection<LancamentoProduto> criarProdutos(HttpServletRequest request, Comanda comanda){
		String[] ids = request.getParameterValues("guidProduto");
		if(ids == null){
			return new ArrayList<>();
		}
		String[] datasCriacao = request.getParameterValues("dataCriacaoProduto");
		String[] produtosIds = request.getParameterValues("produtoId");
		String[] vendedoresIds = request.getParameterValues("vendedorId");
		String[] quantidades = request.getParameterValues("quantidadeProduto");
		Map<String, LancamentoProduto> result = new LinkedHashMap<String, LancamentoProduto>();
		for(int i = 0; i < ids.length; i++){
			LancamentoProduto lancamentoProduto = new LancamentoProduto();
			
			try {
				lancamentoProduto.setComanda(comanda);
				lancamentoProduto.setDataCriacao(Utils.dateTimeFormat.parse(datasCriacao[i]));
				lancamentoProduto.setProduto(findProduto(Long.parseLong(produtosIds[i])));
				lancamentoProduto.setVendedor(findPessoa(Long.parseLong(vendedoresIds[i])));
				lancamentoProduto.setQuantidadeUtilizada(Long.parseLong(quantidades[i]));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			
			result.put(ids[i], lancamentoProduto);
		}
		
		return result.values();
	}
	
	private Collection<LancamentoServico> criarServicos(HttpServletRequest request, Comanda comanda){
		String[] ids = request.getParameterValues("guidServico");
		if(ids == null){
			return new ArrayList<>();
		}
		String[] datasCriacao = request.getParameterValues("dataCriacaoServico");
		String[] servicosIds = request.getParameterValues("servicoId");
		String[] funcionariosIds = request.getParameterValues("funcionarioId");
		String[] assistentesIds = request.getParameterValues("assistenteId");
		Map<String, LancamentoServico> result = new LinkedHashMap<>();
		for(int i = 0; i < ids.length; i++){
			String id = ids[i];
			LancamentoServico lancamento = new LancamentoServico();
			
			try {
				lancamento.setComanda(comanda);
				lancamento.setDataCriacao(Utils.dateTimeFormat.parse(datasCriacao[i]));
				lancamento.setFuncionario(findPessoa(Long.parseLong(funcionariosIds[i])));
				lancamento.setServico(findServico(Long.parseLong(servicosIds[i])));
				lancamento.setAssistente(findPessoa(Long.parseLong(assistentesIds[i])));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			result.put(id, lancamento);
		}
		
		result = criarLancamentosProdutosServicos(request, result);
		
		return result.values();
	}
	
	private Map<String, LancamentoServico> criarLancamentosProdutosServicos(HttpServletRequest request, Map<String, LancamentoServico> lancamentos){
		String[] ids = request.getParameterValues("guidProdutoServico");
		if(ids == null){
			return lancamentos;
		}
		String[] produtodIds = request.getParameterValues("produtoServicoId");
		String[] quantidades = request.getParameterValues("quantidadeProdutoServico");
		for(int i = 0; i < ids.length; i++){
			LancamentoProduto lancamentoProduto = new LancamentoProduto();
			lancamentoProduto.setProduto(findProduto(Long.parseLong(produtodIds[i])));
			lancamentoProduto.setQuantidadeUtilizada(Long.parseLong(quantidades[i]));
			LancamentoServico lancamentoServico = lancamentos.get(ids[i]);
			lancamentoServico.getProdutosUtilizados().add(lancamentoProduto);
		}
		
		return lancamentos;
	}
	
	private Pessoa findPessoa(Long id){
		Pessoa pessoa = pessoas.get(id);
		if(pessoa == null){
			pessoa = pessoaService.findById(id);
			pessoas.put(id, pessoa);
		}
		return pessoa;
	}

	private Servico findServico(Long id){
		Servico servico = servicos.get(id);
		if(servico == null){
			servico = servicoService.findById(id);
			servicos.put(id, servico);
		}
		return servico;
	}
	
	private Produto findProduto(Long id){
		Produto produto = produtos.get(id);
		if(produto == null){
			produto = produtoService.findById(id);
			produtos.put(id, produto);
		}
		return produto;
	}
	
	private List<Comanda> limparComandasJSON(List<Comanda> comandas){
		for(Comanda comanda : comandas){
			limparComandaJSON(comanda);
		}
		return comandas;
	}
	
	private Comanda limparComandaJSON(Comanda comanda){
		if(comanda != null){
			for(LancamentoProduto produto : comanda.getProdutos()){
				produto.setComanda(null);
			}
			for(LancamentoServico servico : comanda.getServicos()){
				servico.setComanda(null);
			}
			comanda.setEstoque(null);
			for(Pagamento pagamento : comanda.getPagamentos()){
				limarPagamentoJSON(pagamento);
			}
		}
		return comanda;
	}
	
}
