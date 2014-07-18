package br.com.min.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LancamentoProduto {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Produto produto;
	private Long quantidadeUtilizada;
	private Date dataCriacao;
	@ManyToOne
	private Comanda comanda;
	@ManyToOne
	private Pessoa vendedor;
	private Boolean lancado;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Comanda getComanda() {
		return comanda;
	}
	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}
	public Pessoa getVendedor() {
		return vendedor;
	}
	public void setVendedor(Pessoa vendedor) {
		this.vendedor = vendedor;
	}
	public Boolean getLancado() {
		return lancado;
	}
	public void setLancado(Boolean lancado) {
		this.lancado = lancado;
	}
	public Long getQuantidadeUtilizada() {
		return quantidadeUtilizada;
	}
	public void setQuantidadeUtilizada(Long quantidadeUtilizada) {
		this.quantidadeUtilizada = quantidadeUtilizada;
	}
	
}
