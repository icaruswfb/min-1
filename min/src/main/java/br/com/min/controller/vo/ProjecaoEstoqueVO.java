package br.com.min.controller.vo;

import br.com.min.entity.Produto;

public class ProjecaoEstoqueVO {

	private Produto produto;
	private Long estoqueAtual;
	private Double estoqueAtualUnidade;
	private Double projecaoCompraMensal;
	private Long consumidoPeriodo;
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Long getEstoqueAtual() {
		return estoqueAtual;
	}
	public void setEstoqueAtual(Long estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}
	public Double getProjecaoCompraMensal() {
		return projecaoCompraMensal;
	}
	public void setProjecaoCompraMensal(Double projecaoCompraMensal) {
		this.projecaoCompraMensal = projecaoCompraMensal;
	}
	public Long getConsumidoPeriodo() {
		return consumidoPeriodo;
	}
	public void setConsumidoPeriodo(Long consumidoPeriodo) {
		this.consumidoPeriodo = consumidoPeriodo;
	}
	public Double getEstoqueAtualUnidade() {
		return estoqueAtualUnidade;
	}
	public void setEstoqueAtualUnidade(Double estoqueAtualUnidade) {
		this.estoqueAtualUnidade = estoqueAtualUnidade;
	}
}
