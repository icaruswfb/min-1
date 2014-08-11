package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.min.entity.FormaPagamento;

public class TotaisPorFormaPagamentoVO {

	private FormaPagamento formaPagamento;
	private Double total = 0.0;
	private List<TotalParcelamentoVO> parcelas = new ArrayList<>();
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<TotalParcelamentoVO> getParcelas() {
		return parcelas;
	}
	public void setParcelas(List<TotalParcelamentoVO> parcelas) {
		this.parcelas = parcelas;
	}
	
}
