package br.com.min.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.controller.vo.ClientesPorAnoCadastroVO;
import br.com.min.controller.vo.ClientesPorMesCadastroVO;
import br.com.min.controller.vo.ClientesPorVisitasVO;
import br.com.min.controller.vo.ComandasClienteReportVO;
import br.com.min.dao.ComandaDAO;
import br.com.min.dao.ComissaoDAO;
import br.com.min.dao.GenericDAO;
import br.com.min.dao.HistoricoDAO;
import br.com.min.dao.PessoaDAO;
import br.com.min.dao.ProdutoDAO;
import br.com.min.dao.ServicoDAO;
import br.com.min.entity.Comanda;
import br.com.min.entity.Comissao;
import br.com.min.entity.Historico;
import br.com.min.entity.Kit;
import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Produto;
import br.com.min.entity.ProdutoQuantidade;
import br.com.min.entity.Servico;
import br.com.min.entity.TipoComissao;
import br.com.min.entity.TipoLancamentoEstoque;
import br.com.min.entity.TipoServico;
import br.com.min.utils.Utils;

@Service
public class ComandaService extends BaseService<Comanda, ComandaDAO>{
	
	@Autowired
	private ComandaDAO dao;
	@Autowired
	private GenericDAO genericDao;
	@Autowired
	private HistoricoDAO historicoDao;
	@Autowired
	private ProdutoDAO produtoDao;
	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private ComissaoDAO comissaDao;
	@Autowired
	private PessoaDAO pessoaDao;
	@Autowired
	private ServicoDAO servicoDao;
	@Transactional
	public Comanda persist(Comanda entity, Pessoa usuarioLogado){
		return persist(entity, false, usuarioLogado);
	}
	
	@Transactional
	public Pagamento pagar(Pagamento pagamento){
		return dao.pagar(pagamento);
	}
	
	public Comanda addServico(LancamentoServico servico, Comanda comanda, Pessoa usuarioLogado){
		servico.setComanda(comanda);
		comanda.getServicos().add(servico);
		comanda = persist(comanda, usuarioLogado);
		
		Historico historico = new Historico();
		historico.setCliente(comanda.getCliente());
		historico.setData(new Date());
		historico.setFuncionario(historico.getFuncionario());
		historico.setTexto("Serviço adicionada à comanda " + comanda.getId() + ": " + servico.getServico().getNome() + " no valor de R$" + servico.getValor());
		historico.setCriador(usuarioLogado);
		historico.setTextoPequeno(Utils.dateTimeFormat.format(historico.getData()) + " - por "+usuarioLogado.getNome());
		genericDao.persist(historico);
		return cleanResult(comanda);
	}
	
	public Comanda addKit(Kit kit, Comanda comanda, Pessoa usuarioLogado){
		for(ProdutoQuantidade produtoQuantidade : kit.getProdutos()){
			LancamentoProduto lancamentoProduto = new LancamentoProduto();
			lancamentoProduto.setComanda(comanda);
			lancamentoProduto.setDataCriacao(new Date());
			lancamentoProduto.setProduto(produtoQuantidade.getProduto());
			lancamentoProduto.setQuantidadeUtilizada(produtoQuantidade.getQuantidade());
			lancamentoProduto.setRevenda(false);
			lancamentoProduto.setValor(produtoQuantidade.getProduto().getPrecoRevenda() * produtoQuantidade.getQuantidade());
			comanda.getProdutos().add(lancamentoProduto);

			crearHistoricoLancamentoProduto(comanda, lancamentoProduto, usuarioLogado);
		}
		comanda = persist(comanda, usuarioLogado);
		return cleanResult(comanda);
	}
	
	public Comanda addProduto(LancamentoProduto lancamentoProduto,
			Comanda comanda, Pessoa usuarioLogado) {
		Double custo = lancamentoProduto.getQuantidadeUtilizada() * lancamentoProduto.getProduto().getCustoUnitario();
		lancamentoProduto.setCusto(custo);
		comanda.getProdutos().add(lancamentoProduto);
		comanda = persist(comanda, usuarioLogado);
		
		crearHistoricoLancamentoProduto(comanda, lancamentoProduto, usuarioLogado);
		
		return cleanResult(comanda);
	}

	private void crearHistoricoLancamentoProduto(Comanda comanda, LancamentoProduto lancamentoProduto, Pessoa usuarioLogado){
		Historico historico = new Historico();
		historico.setCliente(comanda.getCliente());
		historico.setData(new Date());
		historico.setFuncionario(historico.getFuncionario());
		historico.setTexto("Produto adicionada à comanda " + comanda.getId() + ": " + lancamentoProduto.getProduto().getNome() + " no valor de R$" + lancamentoProduto.getValor());
		historico.setCriador(usuarioLogado);
		historico.setTextoPequeno(Utils.dateTimeFormat.format(historico.getData()) + " - por "+usuarioLogado.getNome());
		genericDao.persist(historico);
	}
	
	@Transactional
	public Comanda persist(Comanda entity, boolean isFechamento, Pessoa usuarioLogado){
		if(entity.getId() == null){
			gravarHistoricoAbertura(entity, usuarioLogado);
		}
		entity.setUltimaAtualizacao(new Date().getTime());
		if(isFechamento){
			gravarHistoricoFechamento(entity, usuarioLogado);
		}
		List<Produto> produtosParaAtualizar = calcularValores(entity);
		entity = (Comanda)genericDao.persist(entity);
		for(Produto produto : produtosParaAtualizar){
			produtoService.atualizarSituacaoEstoque(produto);
		}
		if(isFechamento){
			calcularComissoes(entity);
		}
		return cleanResult(entity);
	}
	
	public Double findTotalCobradoPorCliente(Long clienteId){
		return dao.findTotalCobradoPorCliente(clienteId);
	}
	
	public Double findTotalPagoPorCliente(Long clienteId){
		return dao.findTotalPagoPorCliente(clienteId);
	}

	public Double getCreditoCliente(Long clienteId){
		Double pago = findTotalPagoPorCliente(clienteId);
		Double cobrado = findTotalCobradoPorCliente(clienteId);
		Double result = pago - cobrado;
		return result;
	}
	
	public Comanda abrirComanda(Long clienteId, Pessoa criador){
		Comanda comanda = dao.findComandaAberta(clienteId);
		if(comanda == null){
			comanda = new Comanda();
			Pessoa cliente = pessoaDao.findById(clienteId);
			comanda.setCliente(cliente);
			comanda.setAbertura(new Date());
			comanda.setCredito(getCreditoCliente(clienteId));
			
			comanda = persist(comanda, criador);
		}
		return comanda;
	}
	
	public Comanda findComandaAberta(Long clienteId){
		return cleanResult(dao.findComandaAberta(clienteId));
	}
	
	public Long findUltimaAtualizacao(Long comandaId){
		return dao.findUltimaAtualizacao(comandaId);
	}
	
	private LancamentoComissao criarComissao(Double valor, Pessoa funcionario, Pessoa auxiliar, TipoComissao tipo, Comanda comanda, TipoServico tipoServico){
		LancamentoComissao comissao = new LancamentoComissao();
		comissao.setComanda(comanda);
		comissao.setDataCriacao(new Date());
		comissao.setFuncionario(funcionario);
		comissao.setTipo(tipo);
		comissao.setValorVenda(valor);
		comissao.setTipoSevico(tipoServico);
		if(auxiliar != null){
			comissao.setPercentualReduzido(auxiliar.getComissao().getComissaoAuxiliar());
		}

		return comissao;
	}
	
	private List<Produto> calcularValores(Comanda comanda){
		double valorPago = 0;
		for(Pagamento pagamento : comanda.getPagamentos()){
			valorPago += pagamento.getValor();
		}
		Double total = 0d;
		Map<Produto, Long> produtosUtilizados = new HashMap<Produto, Long>();
		for(LancamentoProduto produto : comanda.getProdutos()){
			Produto p = produto.getProduto();
			if(p != null){
				total += p.getPrecoRevenda() * produto.getQuantidadeUtilizada();
				contarQuantidadeUtilizada(p, produto.getQuantidadeUtilizada(), produtosUtilizados);
			}
		}
		for(LancamentoServico servico : comanda.getServicos()){
			if(servico.getServico() != null){
				total += servico.getServico().getPreco();
			}
			for(LancamentoProduto produto : servico.getProdutosUtilizados()){
				Produto p = produto.getProduto();
				if(p != null){
					total += produto.getProduto().getPrecoRevenda() * produto.getQuantidadeUtilizada();
					contarQuantidadeUtilizada(p, produto.getQuantidadeUtilizada(), produtosUtilizados);
				}
			}
		}
		if(comanda.getDesconto() == null){
			comanda.setDesconto(0d);
		}
		if(comanda.getDescontoPromocional() == null){
			comanda.setDescontoPromocional(0d);
		}
		comanda.setValorCobrado(total - (comanda.getDesconto() + comanda.getDescontoPromocional() ) );
		comanda.setValorTotal(total);
		comanda.setValorPago(valorPago);
		return criarLancamentosEstoque(comanda, produtosUtilizados);
	}
	
	private List<LancamentoComissao> gerarComissoes(Comanda comanda){
		List<LancamentoComissao> comissoes = new ArrayList<>();
		for(LancamentoProduto produto : comanda.getProdutos()){
			Produto p = produto.getProduto();
			if(p != null){
				if(produto.getVendedor() != null){
					comissoes.add(criarComissao((p.getPrecoRevenda() * produto.getQuantidadeUtilizada()), produto.getVendedor(), null, TipoComissao.VENDA, comanda, null));
				}
			}
		}
		for(LancamentoServico servico : comanda.getServicos()){
			if(servico.getServico() != null){
				if(servico.getServico().getComicionado()){
					if(servico.getFuncionario() != null){
						TipoComissao tipo = TipoComissao.SERVICO;
						Pessoa assistente = servico.getAssistente();
						if(assistente != null){
							tipo = TipoComissao.SERVICO_COM_AUXILIAR;
							comissoes.add(criarComissao(servico.getServico().getPreco(), assistente, null, TipoComissao.AUXILIAR, comanda, null));
						}
						comissoes.add(criarComissao(servico.getServico().getPreco(), servico.getFuncionario(), assistente, tipo, comanda, servico.getServico().getTipoServico()));
					}
				}
			}
		}
		return comissoes;
	}
	
	private void calcularComissoes(Comanda comanda){
		List<LancamentoComissao> comissoes = gerarComissoes(comanda);
		
		Map<Long, Double> vendasProdutoPorCliente = new HashMap<>();
		List<LancamentoComissao> lancamentosToAdd = new ArrayList<>();
		
		
		Double valorTotal = comanda.getValorTotal() - comanda.getDesconto();
		Double descontoPromocional = comanda.getDescontoPromocional();
		Double percentualDescontoPromocional = 1 - (descontoPromocional / valorTotal );
		
		for(LancamentoComissao comissao : comissoes){
			if(comissao.getTipo().equals(TipoComissao.SERVICO_COM_AUXILIAR)){
				Double percentual = comissao.getFuncionario().getComissao().findComissaoServico(comissao.getTipoSevico()).getComissao() - comissao.getPercentualReduzido();
				comissao.setPercentual(percentual);
			} else if(comissao.getTipo().equals(TipoComissao.SERVICO)){
				Double percentual = comissao.getFuncionario().getComissao().findComissaoServico(comissao.getTipoSevico()).getComissao();
				comissao.setPercentual(percentual);
			} else if(comissao.getTipo().equals(TipoComissao.AUXILIAR)){
				Double percentual = comissao.getFuncionario().getComissao().getComissaoAuxiliar();
				comissao.setPercentual(percentual);
			} else if(comissao.getTipo().equals(TipoComissao.VENDA)){
				Double vendasProduto = vendasProdutoPorCliente.get(comissao.getFuncionario().getId());
				if(vendasProduto == null){
					List<LancamentoComissao> lancamentos = comissaDao.findLancamentosComissaoVendaDoMes(comissao.getFuncionario(), comissao.getDataCriacao(), TipoComissao.VENDA);
					vendasProduto = 0d;
					for(LancamentoComissao lancamento : lancamentos){
						vendasProduto += lancamento.getValorVenda();
					}
					vendasProdutoPorCliente.put(comissao.getFuncionario().getId(), vendasProduto);
				}
				Comissao comissaoFuncionario = comissao.getFuncionario().getComissao();
				Double venda = comissao.getValorVenda();
				Double range1 = comissaoFuncionario.getValorRange1() == null ? Double.MAX_VALUE : comissaoFuncionario.getValorRange1();
				Double range2 = comissaoFuncionario.getValorRange2() == null ? Double.MAX_VALUE : comissaoFuncionario.getValorRange2();
				Double range3 = comissaoFuncionario.getValorRange3() == null ? Double.MAX_VALUE : comissaoFuncionario.getValorRange3();
				Double[] ranges = {range1, range2, range3, 
												Double.MAX_VALUE};
				Double[] percents = {comissaoFuncionario.getComissaoRange1(), 
													comissaoFuncionario.getComissaoRange2(), 
													comissaoFuncionario.getComissaoRange3(), 
													comissaoFuncionario.getComissaoRange4()};
				int index;
				if(vendasProduto < comissaoFuncionario.getValorRange1()){
					index = 0;
				}else if( vendasProduto < comissaoFuncionario.getValorRange2()){
					index = 1;
				}else if( vendasProduto < comissaoFuncionario.getValorRange3() ){
					index = 2;
				}else{
					index = 3;
				}
				lancamentosToAdd.addAll(calcularPercentualPorRange(ranges, percents, vendasProduto, comissao, comanda, index));
				vendasProduto += venda;
				vendasProdutoPorCliente.put(comissao.getFuncionario().getId(), vendasProduto);
			}

			comissao.setValor( ( ((comissao.getValorVenda()* percentualDescontoPromocional) / 100)  ) * comissao.getPercentual() );
		}
		
		for(LancamentoComissao comissao : lancamentosToAdd){
			comissao.setValor( ( (comissao.getValorVenda() / 100) * comissao.getPercentual() ) * percentualDescontoPromocional );
		}
		comissoes.addAll(lancamentosToAdd);
		comanda.getComissoes().addAll(comissoes);
		genericDao.persist(comanda);
	}
	
	private List<LancamentoComissao> calcularPercentualPorRange(
					Double[] ranges, Double[] percents, Double vendasProduto, 
					LancamentoComissao comissao, 
					Comanda comanda, int index){
		List<LancamentoComissao> lancamentosToAdd = new ArrayList<>();
		Double percentual = 0d;
		Double novoValor = (vendasProduto + comissao.getValorVenda()); 
		if( novoValor  <= ranges[index] ){
		
			percentual = percents[index];
		}else{
			Double diferencaAcima = novoValor - ranges[index];
			Double diferencaAbaixo = ranges[index] - vendasProduto;
			comissao.setValorVenda(diferencaAbaixo);
			percentual = percents[index];
			LancamentoComissao novaComissao = criarComissao(diferencaAcima, comissao.getFuncionario(), null, TipoComissao.VENDA, comanda, null);
			index++;
			novaComissao.setPercentual(percents[index]);
			lancamentosToAdd.addAll(calcularPercentualPorRange(ranges, percents, vendasProduto + diferencaAbaixo, novaComissao, comanda, index));
			
			lancamentosToAdd.add(novaComissao);
			vendasProduto += diferencaAbaixo;
		}
		comissao.setPercentual(percentual);
		return lancamentosToAdd;
	}
	
	private List<Produto> criarLancamentosEstoque(Comanda comanda, Map<Produto, Long> produtosUtilizados){
		Set<Entry<Produto, Long>> entries = produtosUtilizados.entrySet();
		List<LancamentoEstoque> toAdd = new ArrayList<LancamentoEstoque>();
		List<Produto> produtosParaAtualizar = new ArrayList<Produto>();
		for(Entry<Produto, Long> entry : entries){
			produtosParaAtualizar.add(entry.getKey());
			boolean criarNovo = true;
			
			for(LancamentoEstoque existente : comanda.getEstoque()){
				if(entry.getKey().equals(existente.getProduto())){
					criarNovo = false;
					if( ! existente.getQuantidade().equals(entry.getValue())){
						existente.setDataCriacao(new Date());
						existente.setQuantidade(entry.getValue());
					}
					toAdd.add(existente);
					break;
				}
			}
			if(criarNovo){
				LancamentoEstoque lancamento = new LancamentoEstoque();
				lancamento.setComanda(comanda);
				lancamento.setDataCriacao(new Date());
				lancamento.setProduto(entry.getKey());
				lancamento.setQuantidade(entry.getValue());
				lancamento.setTipo(TipoLancamentoEstoque.SAIDA);
				
				toAdd.add(lancamento);
			}
		}
		if(comanda.getEstoque() != null){
			for(LancamentoEstoque existente : comanda.getEstoque()){
				boolean isToRemove = true;
				for(Entry<Produto, Long> entry : entries){
					if(entry.getKey().equals(existente.getProduto())){
						isToRemove = false;
						break;
					}
				}
				if(isToRemove){
					produtosParaAtualizar.add(existente.getProduto());
				}
			}
		}
		if( ! toAdd.isEmpty()){
			comanda.setEstoque(toAdd);
		}
		
		return produtosParaAtualizar;
	}
	
	private void contarQuantidadeUtilizada(Produto p, Long quantidade, Map<Produto, Long> produtosUtilizados){
		Long quantidadeTotalUtilizada = produtosUtilizados.get(p);
		if(quantidadeTotalUtilizada == null){
			quantidadeTotalUtilizada = 0L;
		}
		produtosUtilizados.put(p, quantidadeTotalUtilizada + quantidade);
	}
	
	private void gravarHistoricoFechamento(Comanda entity, Pessoa usuarioLogado){
		Historico historico = criarHistorico(entity, usuarioLogado);
		StringBuffer sb = new StringBuffer();
		sb.append("Comanda aberta em ").append(Utils.dateTimeFormat.format(entity.getAbertura()));
		sb.append(" foi fechada em ").append(Utils.dateTimeFormat.format(entity.getFechamento()))
			.append(" no valor de R$").append(entity.getValorCobrado())
			.append(" com R$").append(entity.getValorPago()).append(" pago");
		historico.setTexto(sb.toString());
		genericDao.persist(historico);
	}
	
	private void gravarHistoricoAbertura(Comanda entity, Pessoa usuarioLogado){
		Historico historico = criarHistorico(entity, usuarioLogado);
		StringBuffer sb = new StringBuffer();
		sb.append("Comanda aberta em ").append(Utils.dateTimeFormat.format(entity.getAbertura()));
		historico.setTexto(sb.toString());
		genericDao.persist(historico);
	}
	
	private Historico criarHistorico(Comanda comanda, Pessoa usuarioLogado){
		Historico historico = new Historico();
		historico.setCriador(usuarioLogado);
		historico.setData(new Date());
		historico.setCliente(comanda.getCliente());
		StringBuffer textoPequeno = new StringBuffer();
		textoPequeno.append(Utils.dateTimeFormat.format(historico.getData()));
		if(historico.getCriador() != null){
			textoPequeno.append(" - criado por ").append(historico.getCriador().getNome());
		}
		historico.setTextoPequeno(textoPequeno.toString());
		return historico;
	}
	
	@Transactional
	public List<Comanda> find(Comanda entity, boolean fechadas){
		return cleanResult(dao.find(entity, fechadas));
	}
	@Transactional
	public List<Comanda> find(Comanda entity){
		return cleanResult(dao.find(entity, null));
	}
	
	public List<Comanda> findFechamento(){
		return cleanResult(dao.findFechamento(new Date(), new Date()));
	}

	public List<Comanda> findFechamento(Date dataInicio, Date dataFim){
		return cleanResult(dao.findFechamento(dataInicio, dataFim));
	}
	
	public List<Comanda> listar(){
		Comanda entity = new Comanda();
		return cleanResult(dao.find(entity, null));
	}
	
	public List<Comanda> listarPorPeriodo(Date inicio, Date fim, boolean toExport){
		return cleanResult(dao.find(inicio, fim, toExport));
	}
	
	public Comanda findById(Long id){
		Comanda entity = new Comanda();
		entity.setId(id);
		List<Comanda> result = dao.find(entity, null);
		if(result.isEmpty()){
			return null;
		}else{
			return cleanResult(result.get(0));
		}
	}

	public void delete(Long id, Pessoa usuarioLogado) {
		Comanda entity = findById(id);
		Historico historico = new Historico();
		historico.setCliente(entity.getCliente());
		historico.setData(new Date());
		historico.setFuncionario(historico.getFuncionario());
		historico.setTexto("Comanda " + entity.getId() + " no valor de R$" + entity.getValorCobrado() 
				+ ", aberta em " + Utils.dateTimeFormat.format(entity.getAbertura()) + " onde o valor pago foi R$" + entity.getValorPago() 
				+ " foi excluída por " + usuarioLogado.getNome());
		historico.setCriador(usuarioLogado);
		historico.setTextoPequeno(Utils.dateTimeFormat.format(historico.getData()) + " - Comanda "+entity.getId()+" excluída");
		entity.getPagamentos().clear();
		entity.getComissoes().clear();
		entity.getEstoque().clear();
		entity.getProdutos().clear();
		entity.getServicos().clear();
		persist(entity, usuarioLogado);
		Comanda toDelete = new Comanda();
		toDelete.setId(id);
		genericDao.delete(toDelete);

		genericDao.persist(historico);
	}
	public void deletePagamento(Long id) {
		Pagamento entity = new Pagamento();
		entity.setId(id);
		genericDao.delete(entity);
	}

	public LancamentoServico findLancamentoServicoById(Long id){
		LancamentoServico entity = dao.findLancamentoServicoById(id);
		return entity;
	}
	
	private Historico criarHistoricoExclusao(Comanda comanda, Pessoa usuarioLogado){
		Historico historico = new Historico();
		historico.setCliente(comanda.getCliente());
		historico.setCriador(usuarioLogado);
		historico.setData(new Date());
		historico.setTextoPequeno(Utils.dateTimeFormat.format(new Date()) + " - por " + usuarioLogado.getNome());
		
		return historico;
	}
	
	public void deleteLancamentoServico(Long id, Pessoa usuarioLogado) {
		LancamentoServico entity = findLancamentoServicoById(id);
		Comanda comanda = entity.getComanda();
		if(comanda.getFechamento() != null){
			return;
		}
		Historico historico = criarHistoricoExclusao(comanda, usuarioLogado);
		historico.setTexto("Lançamento do serviço " + entity.getServico().getNome() + " no valor de R$" + entity.getValor() + " foi excluído.");
		comanda.getServicos().remove(entity);
		persist(comanda, usuarioLogado);
		genericDao.persist(historico);
	}

	public void deleteLancamentoProduto(Long id, Pessoa usuarioLogado) {
		LancamentoProduto entity = dao.findLancamentoProdutoById(id);
		Comanda comanda = entity.getComanda();
		if(comanda.getFechamento() != null){
			return;
		}
		Historico historico = criarHistoricoExclusao(comanda, usuarioLogado);
		historico.setTexto("Lançamento do produto " + entity.getProduto().getNome() + " no valor de R$" + entity.getValor() + " foi excluído.");
		comanda.getProdutos().remove(entity);
		persist(comanda, usuarioLogado);
		genericDao.persist(historico);
	}

	public void deleteLancamentoProdutoServico(Long id, Pessoa usuarioLogado) {
		LancamentoServico servico = dao.findLancamentoServicoByLancamentoProdutoId(id);
		if(servico.getComanda().getFechamento() != null){
			return;
		}
		LancamentoProduto produto = null;
		for(LancamentoProduto p : servico.getProdutosUtilizados()){
			if(p.getId().equals(id)){
				produto = p;
				break;
			}
		}
		servico.getProdutosUtilizados().remove(produto);
		Historico historico = criarHistoricoExclusao(servico.getComanda(), usuarioLogado);
		historico.setTexto("Lançamento do serviço " + produto.getProduto().getNome() + " no valor de R$" + produto.getValor() + " foi excluído.");
		genericDao.persist(servico);
		genericDao.persist(historico);
	}
	
	public List<Comanda> findComandasByFuncionario(Long funcionarioId){
		return dao.findComandasByFuncionario(funcionarioId);
	}
	
	public List<LancamentoServico> findLancamentoServico(Date inicio, Date fim){
		return dao.findLancamentoServico(inicio, fim);
	}
	
	public List<Comanda> listComandasCliente(Pessoa cliente){
		return cleanResult(dao.listComandasCliente(cliente));
	}
	
	public ComandasClienteReportVO relatorioCliente(){
		ComandasClienteReportVO result = new ComandasClienteReportVO();
		List<ClientesPorAnoCadastroVO> clientesPorAno = result.getClientesPorAno();
		List<ClientesPorVisitasVO> clientesPorVisitas = result.getClientesPorVisitas();

		Pessoa pessoa = new Pessoa();
		pessoa.setFuncionario(false);
		List<Pessoa> clientes = pessoaDao.findPessoa(pessoa);
		
		for(Pessoa cliente : clientes){
			List<Comanda> comandas = getDAO().listComandasCliente(cliente);
			if( ! comandas.isEmpty() ){
				Comanda primeiraComanda = comandas.get(0);
				Calendar dataAbertura = Calendar.getInstance();
				dataAbertura.setTime(primeiraComanda.getAbertura());
				
				ClientesPorAnoCadastroVO ano = null;
				for(ClientesPorAnoCadastroVO porAno : clientesPorAno){
					if(porAno.getAno().equals(dataAbertura.get(Calendar.YEAR))){
						ano = porAno;
						break;
					}
				}
				if(ano == null){
					ano = new ClientesPorAnoCadastroVO();
					ano.setAno(dataAbertura.get(Calendar.YEAR));
					clientesPorAno.add(ano);
				}
				ano.setTotal(ano.getTotal() + 1);
				
				
				ClientesPorMesCadastroVO mes = null;
				for(ClientesPorMesCadastroVO porMes : ano.getClientesPorMes()){
					if(porMes.getMes().equals(dataAbertura.get(Calendar.MONTH))){
						mes = porMes;
						break;
					}
				}
				if(mes == null){
					mes = new ClientesPorMesCadastroVO();
					mes.setMes(dataAbertura.get(Calendar.MONTH));
					ano.getClientesPorMes().add(mes);
				}
				mes.setQuantidade(mes.getQuantidade() + 1);
				if(comandas.size() > 1){
					mes.setRetornos(mes.getRetornos() + 1);
				}
				
				ClientesPorVisitasVO visitas = null;
				for(ClientesPorVisitasVO porVisitas : clientesPorVisitas){
					if(porVisitas.getVisitas().equals(comandas.size())){
						visitas = porVisitas;
						break;
					}
				}
				if(visitas == null){
					visitas = new ClientesPorVisitasVO();
					visitas.setVisitas(comandas.size());
					clientesPorVisitas.add(visitas);
				}
				boolean countedManicure = false;
				boolean countedCabelo = false;
				boolean countedMaquiagem = false;
				boolean countedEstetica = false;
				boolean countedOutros = false;
				for(Comanda comanda : comandas){
					for(LancamentoServico servico : comanda.getServicos()){
						if( TipoServico.CABELO_ID.equals( servico.getServico().getTipoServico().getId() ) 
								&& ! countedCabelo){
							visitas.setCabelo( visitas.getCabelo() + 1);
							countedCabelo = true;
							continue;
						}
						if( TipoServico.UNHA_ID.equals( servico.getServico().getTipoServico().getId() )  
								&& ! countedManicure){
							visitas.setManicure( visitas.getManicure() + 1);
							countedManicure = true;
							continue;
						}
						if( TipoServico.MAQUIAGEM_ID.equals( servico.getServico().getTipoServico().getId() )  
								&& ! countedMaquiagem){
							visitas.setMaquiagem( visitas.getMaquiagem() + 1);
							countedMaquiagem = true;
							continue;
						}
						if( TipoServico.ESTETICA_ID.equals( servico.getServico().getTipoServico().getId() ) 
								&& ! countedEstetica){
							visitas.setEstetica( visitas.getEstetica() + 1);
							countedEstetica = true;
							continue;
						}
						if( TipoServico.OUTROS_ID.equals( servico.getServico().getTipoServico().getId() ) 
								&& ! countedOutros){
							visitas.setOutros( visitas.getOutros() + 1);
							countedOutros = true;
							continue;
						}
					}
				}
				visitas.setClientes(visitas.getClientes() + 1);
			}
		}

		BeanComparator beanComparator = new BeanComparator("ano");
		Collections.sort(clientesPorAno, beanComparator);
		
		beanComparator = new BeanComparator("mes");
		for(ClientesPorAnoCadastroVO ano : clientesPorAno){
			Collections.sort(ano.getClientesPorMes(), beanComparator);
		}
		
		beanComparator = new BeanComparator("visitas");
		Collections.sort(clientesPorVisitas, beanComparator);
		
		return result;
	}
	
	public LancamentoServico criarLancamentoServico(Long servicoId, Long funcionarioId, Long assistenteId, Long clienteId){
		LancamentoServico lancamentoServico = new LancamentoServico();
		Pessoa funcionario = pessoaDao.findById(funcionarioId);
		Pessoa assistente = null;
		if(assistenteId != null){
			assistente = pessoaDao.findById(assistenteId);
		}
		Servico servico = servicoDao.findById(servicoId);
		
		lancamentoServico.setAssistente(assistente);
		lancamentoServico.setDataCriacao(new Date());
		lancamentoServico.setFuncionario(funcionario);
		lancamentoServico.setServico(servico);
		lancamentoServico.setValor(servico.getPreco());
	
		return lancamentoServico;
	}
	
	public Comanda addServico(Long servicoId, Long funcionarioId, Long assistenteId, Long clienteId, Pessoa criador){
		Comanda comanda = dao.findComandaAberta(clienteId);
		LancamentoServico lancamentoServico = criarLancamentoServico(servicoId, funcionarioId, assistenteId, clienteId);
		comanda = addServico(lancamentoServico, comanda, criador);
		return comanda;
	}
	
}
