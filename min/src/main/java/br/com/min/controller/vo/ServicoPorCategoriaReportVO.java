package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.min.entity.TipoServico;

public class ServicoPorCategoriaReportVO {

	private TipoServico tipoServico;
	private List<ServicoReportVO> servicos = new ArrayList<>();
	private Long totalExecucoes = 0L;
	
	public TipoServico getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
	public List<ServicoReportVO> getServicos() {
		return servicos;
	}
	public void setServicos(List<ServicoReportVO> servicos) {
		this.servicos = servicos;
	}
	public Long getTotalExecucoes() {
		return totalExecucoes;
	}
	public void setTotalExecucoes(Long totalExecucoes) {
		this.totalExecucoes = totalExecucoes;
	}
	
}
