package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

public class ComandasClienteReportVO {

	private List<ClientesPorAnoCadastroVO> clientesPorAno = new ArrayList<ClientesPorAnoCadastroVO>();
	private List<ClientesPorVisitasVO> clientesPorVisitas = new ArrayList<ClientesPorVisitasVO>();
	
	public List<ClientesPorAnoCadastroVO> getClientesPorAno() {
		return clientesPorAno;
	}
	public void setClientesPorAno(List<ClientesPorAnoCadastroVO> clientesPorAno) {
		this.clientesPorAno = clientesPorAno;
	}
	public List<ClientesPorVisitasVO> getClientesPorVisitas() {
		return clientesPorVisitas;
	}
	public void setClientesPorVisitas(List<ClientesPorVisitasVO> clientesPorVisitas) {
		this.clientesPorVisitas = clientesPorVisitas;
	}
	
}
