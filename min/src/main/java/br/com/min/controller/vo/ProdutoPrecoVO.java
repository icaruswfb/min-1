package br.com.min.controller.vo;

import br.com.min.entity.Produto;

public class ProdutoPrecoVO {

	private Produto produto;
	private Double percentual;
	private Double diferenca;
	private Double precoNovo;
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Double getPercentual() {
		return percentual;
	}
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
	public Double getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(Double diferenca) {
		this.diferenca = diferenca;
	}
	public Double getPrecoNovo() {
		return precoNovo;
	}
	public void setPrecoNovo(Double precoNovo) {
		this.precoNovo = precoNovo;
	}
	
}
