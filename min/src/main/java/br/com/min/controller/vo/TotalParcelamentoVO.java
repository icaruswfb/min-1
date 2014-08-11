package br.com.min.controller.vo;

public class TotalParcelamentoVO implements Comparable<TotalParcelamentoVO> {

	private Integer parcelas;
	private Double total = 0.0;
	public Integer getParcelas() {
		return parcelas;
	}
	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public int compareTo(TotalParcelamentoVO o) {
		return this.getParcelas() - o.getParcelas();
	}
	
}
