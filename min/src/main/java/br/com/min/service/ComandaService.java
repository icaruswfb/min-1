package br.com.min.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.ComandaDAO;
import br.com.min.dao.GenericDAO;
import br.com.min.dao.HistoricoDAO;
import br.com.min.dao.ProdutoDAO;
import br.com.min.entity.Comanda;
import br.com.min.entity.Historico;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Produto;
import br.com.min.entity.TipoLancamentoEstoque;
import br.com.min.utils.Utils;

@Service
public class ComandaService {
	
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

	@Transactional
	public Comanda persist(Comanda entity){
		return persist(entity, false);
	}
	
	@Transactional
	public Pagamento pagar(Pagamento pagamento){
		return dao.pagar(pagamento);
	}
	
	@Transactional
	public Comanda persist(Comanda entity, boolean isFechamento){
		if(entity.getId() == null){
			gravarHistoricoAbertura(entity);
		}
		if(isFechamento){
			gravarHistoricoFechamento(entity);
		}
		List<Produto> produtosParaAtualizar = calcularValores(entity);
		entity = (Comanda)genericDao.persist(entity);
		for(Produto produto : produtosParaAtualizar){
			produtoService.atualizarSituacaoEstoque(produto);
		}
		return entity;
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
	
	public Comanda findComandaAberta(Long clienteId){
		return dao.findComandaAberta(clienteId);
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
			total += p.getPrecoRevenda() * produto.getQuantidadeUtilizada();
			contarQuantidadeUtilizada(p, produto.getQuantidadeUtilizada(), produtosUtilizados);
		}
		for(LancamentoServico servico : comanda.getServicos()){
			total += servico.getServico().getPreco();
			for(LancamentoProduto produto : servico.getProdutosUtilizados()){
				Produto p = produto.getProduto();
				total += produto.getProduto().getPrecoRevenda() * produto.getQuantidadeUtilizada();
				contarQuantidadeUtilizada(p, produto.getQuantidadeUtilizada(), produtosUtilizados);
			}
		}
		if(comanda.getDesconto() == null){
			comanda.setDesconto(0d);
		}
		comanda.setValorCobrado(total - comanda.getDesconto());
		comanda.setValorTotal(total);
		comanda.setValorPago(valorPago);
		return criarLancamentosEstoque(comanda, produtosUtilizados);
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
		comanda.setEstoque(toAdd);
		
		return produtosParaAtualizar;
	}
	
	private void contarQuantidadeUtilizada(Produto p, Long quantidade, Map<Produto, Long> produtosUtilizados){
		Long quantidadeTotalUtilizada = produtosUtilizados.get(p);
		if(quantidadeTotalUtilizada == null){
			quantidadeTotalUtilizada = 0L;
		}
		produtosUtilizados.put(p, quantidadeTotalUtilizada + quantidade);
	}
	
	private void gravarHistoricoFechamento(Comanda entity){
		Historico historico = criarHistorico(entity);
		StringBuffer sb = new StringBuffer();
		sb.append("Comanda aberta em ").append(Utils.dateTimeFormat.format(entity.getAbertura()));
		sb.append(" foi fechada em ").append(Utils.dateTimeFormat.format(entity.getFechamento()))
			.append(" no valor de R$").append(entity.getValorCobrado())
			.append(" com R$").append(entity.getValorPago()).append(" pago");
		historico.setTexto(sb.toString());
		genericDao.persist(historico);
	}
	
	private void gravarHistoricoAbertura(Comanda entity){
		Historico historico = criarHistorico(entity);
		StringBuffer sb = new StringBuffer();
		sb.append("Comanda aberta em ").append(Utils.dateTimeFormat.format(entity.getAbertura()));
		historico.setTexto(sb.toString());
		genericDao.persist(historico);
	}
	
	private Historico criarHistorico(Comanda comanda){
		Historico historico = new Historico();
		historico.setData(new Date());
		historico.setCliente(comanda.getCliente());
		StringBuffer textoPequeno = new StringBuffer();
		textoPequeno.append(Utils.dateTimeFormat.format(historico.getData()));
		textoPequeno.append(" - criado por TODO");
		historico.setTextoPequeno(textoPequeno.toString());
		return historico;
	}
	
	@Transactional
	public List<Comanda> find(Comanda entity, boolean fechadas){
		return dao.find(entity, fechadas);
	}
	@Transactional
	public List<Comanda> find(Comanda entity){
		return dao.find(entity, null);
	}
	
	public List<Comanda> findFechamento(){
		return dao.findFechamento(new Date());
	}

	public List<Comanda> findFechamento(Date data){
		return dao.findFechamento(data);
	}
	
	public List<Comanda> listar(){
		Comanda entity = new Comanda();
		return dao.find(entity, null);
	}
	
	public Comanda findById(Long id){
		Comanda entity = new Comanda();
		entity.setId(id);
		List<Comanda> result = dao.find(entity, null);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Comanda entity = new Comanda();
		entity.setId(id);
		genericDao.delete(entity);
	}
	public void deletePagamento(Long id) {
		Pagamento entity = new Pagamento();
		entity.setId(id);
		genericDao.delete(entity);
	}
	
}
