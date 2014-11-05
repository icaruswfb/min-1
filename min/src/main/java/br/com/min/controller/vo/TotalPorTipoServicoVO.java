package br.com.min.controller.vo;

import br.com.min.entity.TipoServico;

public class TotalPorTipoServicoVO {

	private TipoServico tipoServico;
	private Double total = 0d;
	
	public TipoServico getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
}
