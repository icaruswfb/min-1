package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;

public class EstoqueVO {
	
	private Long quantidade;
	private Produto produto;
	private List<LancamentoEstoque> lancamentos = new ArrayList<LancamentoEstoque>();
	
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public List<LancamentoEstoque> getLancamentos() {
		return lancamentos;
	}
	public void setLancamentos(List<LancamentoEstoque> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
}
