package br.com.min.controller.vo;

public class ClientesPorMesCadastroVO {

	private Integer mes;
	private Integer quantidade = 0;
	private Integer retornos = 0;
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Integer getRetornos() {
		return retornos;
	}
	public void setRetornos(Integer retornos) {
		this.retornos = retornos;
	}
	
}
