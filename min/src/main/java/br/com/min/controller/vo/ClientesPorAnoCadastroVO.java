package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

public class ClientesPorAnoCadastroVO {

	private Integer ano;
	private List<ClientesPorMesCadastroVO> clientesPorMes = new ArrayList<>();
	private Integer total = 0;
	
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public List<ClientesPorMesCadastroVO> getClientesPorMes() {
		return clientesPorMes;
	}
	public void setClientesPorMes(List<ClientesPorMesCadastroVO> clientesPorMes) {
		this.clientesPorMes = clientesPorMes;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getQuantidadeMeses(){
		return clientesPorMes.size();
	}
}
