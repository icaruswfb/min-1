package br.com.min.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LancamentoComissao extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Pessoa funcionario;
	@ManyToOne
	private Comanda comanda;
	private Double valor;
	private Double valorVenda;
	private Double percentual;
	private Double percentualReduzido;
	private Date dataCriacao;
	@Enumerated(EnumType.STRING)
	private TipoComissao tipo;
	private Date dataPagamento;
	@ManyToOne
	private TipoServico tipoSevico;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Pessoa getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Double getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}
	public Double getPercentual() {
		return percentual;
	}
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public TipoComissao getTipo() {
		return tipo;
	}
	public void setTipo(TipoComissao tipo) {
		this.tipo = tipo;
	}
	public Comanda getComanda() {
		return comanda;
	}
	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}
	public Double getPercentualReduzido() {
		return percentualReduzido;
	}
	public void setPercentualReduzido(Double percentualReduzido) {
		this.percentualReduzido = percentualReduzido;
	}
	
	public String toString(){
		super.toString();
		return getPercentual() + "% de " + getValorVenda();
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public TipoServico getTipoSevico() {
		return tipoSevico;
	}
	public void setTipoSevico(TipoServico tipoSevico) {
		this.tipoSevico = tipoSevico;
	}
	
}
