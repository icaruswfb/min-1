package br.com.min.controller.vo;

public class ClientesPorVisitasVO {

	private Integer visitas;
	private Integer clientes = 0;
	private Integer manicure = 0;
	private Integer cabelo = 0;
	private Integer maquiagem = 0;
	private Integer estetica = 0;
	private Integer outros = 0;
	
	public Integer getVisitas() {
		return visitas;
	}
	public void setVisitas(Integer visitas) {
		this.visitas = visitas;
	}
	public Integer getClientes() {
		return clientes;
	}
	public void setClientes(Integer clientes) {
		this.clientes = clientes;
	}
	public Integer getManicure() {
		return manicure;
	}
	public void setManicure(Integer manicure) {
		this.manicure = manicure;
	}
	public Integer getCabelo() {
		return cabelo;
	}
	public void setCabelo(Integer cabelo) {
		this.cabelo = cabelo;
	}
	public Integer getOutros() {
		return outros;
	}
	public void setOutros(Integer outros) {
		this.outros = outros;
	}
	public Integer getMaquiagem() {
		return maquiagem;
	}
	public void setMaquiagem(Integer maquiagem) {
		this.maquiagem = maquiagem;
	}
	public Integer getEstetica() {
		return estetica;
	}
	public void setEstetica(Integer estetica) {
		this.estetica = estetica;
	}
	
}
