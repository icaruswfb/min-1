package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.min.entity.Servico;

public class ServicoReportVO {

	private Servico servico;
	private Long quantidade = 0L;
	private Double valorTotal = 0d;
	private List<Long> clientesIds = new ArrayList<>();
	private Long quantidadeClientesRecorrentes = 0L; 
	
	public Servico getServico() {
		return servico;
	}
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public List<Long> getClientesIds() {
		return clientesIds;
	}
	public void setClientesIds(List<Long> clientesIds) {
		this.clientesIds = clientesIds;
	}
	public Integer getQuantidadeClientesDiferentes(){
		return clientesIds.size();
	}
	public Long getQuantidadeClientesRecorrentes() {
		return quantidadeClientesRecorrentes;
	}
	public void setQuantidadeClientesRecorrentes(Long quantidadeClientesRecorrentes) {
		this.quantidadeClientesRecorrentes = quantidadeClientesRecorrentes;
	}
	
}
