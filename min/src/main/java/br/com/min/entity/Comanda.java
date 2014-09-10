package br.com.min.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Comanda {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Pessoa cliente;
	@ManyToOne
	private Pessoa criador;
	private Date abertura;
	private Date fechamento;
	@OneToMany(mappedBy="comanda", cascade=CascadeType.ALL,orphanRemoval=true)
	private List<LancamentoServico> servicos = new ArrayList<LancamentoServico>();
	@OneToMany(mappedBy="comanda", cascade=CascadeType.ALL,orphanRemoval=true)
	private List<LancamentoProduto> produtos = new ArrayList<LancamentoProduto>();
	@OneToMany(mappedBy="comanda", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LancamentoEstoque> estoque = new ArrayList<LancamentoEstoque>();
	@OneToMany(mappedBy="comanda", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LancamentoComissao> comissoes = new ArrayList<>();
	private Double valorTotal;
	private Double desconto;
	private Double valorCobrado;
	private Double valorPago;
	private Double credito = 0d;
	@Column()
	private Long ultimaAtualizacao;

	@OneToMany(mappedBy="comanda", cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Pagamento> pagamentos = new ArrayList<Pagamento>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Pessoa getCliente() {
		return cliente;
	}
	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}
	public Date getFechamento() {
		return fechamento;
	}
	public void setFechamento(Date fechamento) {
		this.fechamento = fechamento;
	}
	public List<LancamentoServico> getServicos() {
		return servicos;
	}
	public void setServicos(List<LancamentoServico> servicos) {
		this.servicos = servicos;
	}
	public List<LancamentoProduto> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<LancamentoProduto> produtos) {
		this.produtos = produtos;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Date getAbertura() {
		return abertura;
	}
	public void setAbertura(Date abertura) {
		this.abertura = abertura;
	}
	public Pessoa getCriador() {
		return criador;
	}
	public void setCriador(Pessoa criador) {
		this.criador = criador;
	}
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	public Double getValorCobrado() {
		return valorCobrado;
	}
	public void setValorCobrado(Double valorCobrado) {
		this.valorCobrado = valorCobrado;
	}
	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}
	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	public Double getValorPago() {
		return valorPago;
	}
	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
	public Double getCredito() {
		return credito;
	}
	public void setCredito(Double credito) {
		this.credito = credito;
	}
	public List<LancamentoEstoque> getEstoque() {
		return estoque;
	}
	public void setEstoque(List<LancamentoEstoque> estoque) {
		this.estoque = estoque;
	}
	public List<LancamentoComissao> getComissoes() {
		return comissoes;
	}
	public void setComissoes(List<LancamentoComissao> comissoes) {
		this.comissoes = comissoes;
	}
	public Long getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}
	public void setUltimaAtualizacao(Long ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}
	
	
}
