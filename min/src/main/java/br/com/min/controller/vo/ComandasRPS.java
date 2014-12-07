package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.min.entity.Comanda;

public class ComandasRPS {
	
	private long numeroLote;
	private int quantidadeRps;
	private List<Comanda> comandas = new ArrayList<>();
	
	public long getNumeroLote() {
		return numeroLote;
	}
	public void setNumeroLote(long numeroLote) {
		this.numeroLote = numeroLote;
	}
	public int getQuantidadeRps() {
		return quantidadeRps;
	}
	public void setQuantidadeRps(int quantidadeRps) {
		this.quantidadeRps = quantidadeRps;
	}
	public List<Comanda> getComandas() {
		return comandas;
	}
	public void setComandas(List<Comanda> comandas) {
		this.comandas = comandas;
	}
	
}
