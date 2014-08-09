package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.min.entity.Pessoa;

public class ComissoesPorFuncionarioVO {

	private Pessoa funcionario;
	private Double total = 0.0;
	private Double totalServico = 0.0;
	private Double totalServicoAuxiliado = 0.0;
	private Double totalAuxilar = 0.0;
	private Double totalVendas = 0.0;
	private List<Double> percentuais = new ArrayList<>();
	private Map<Double, Double> totalPorPercentual = new HashMap<Double, Double>();
	
	public Pessoa getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getTotalServico() {
		return totalServico;
	}
	public void setTotalServico(Double totalServico) {
		this.totalServico = totalServico;
	}
	public Double getTotalServicoAuxiliado() {
		return totalServicoAuxiliado;
	}
	public void setTotalServicoAuxiliado(Double totalServicoAuxiliado) {
		this.totalServicoAuxiliado = totalServicoAuxiliado;
	}
	public Double getTotalAuxilar() {
		return totalAuxilar;
	}
	public void setTotalAuxilar(Double totalAuxilar) {
		this.totalAuxilar = totalAuxilar;
	}
	public Double getTotalVendas() {
		return totalVendas;
	}
	public void setTotalVendas(Double totalVendas) {
		this.totalVendas = totalVendas;
	}
	public List<Double> getPercentuais() {
		return percentuais;
	}
	public void setPercentuais(List<Double> percentuais) {
		this.percentuais = percentuais;
	}
	public Map<Double, Double> getTotalPorPercentual() {
		return totalPorPercentual;
	}
	public void setTotalPorPercentual(Map<Double, Double> totalPorPercentual) {
		this.totalPorPercentual = totalPorPercentual;
	}
	
}
