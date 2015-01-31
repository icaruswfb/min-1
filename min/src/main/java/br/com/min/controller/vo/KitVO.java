package br.com.min.controller.vo;

import java.util.ArrayList;
import java.util.List;

public class KitVO {
	
	private Long id;
	private String nome;
	private Double valor;
	private List<ServicoKitVO> servicos = new ArrayList<>();
	private List<ProdutoKitVO> produtos = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public List<ServicoKitVO> getServicos() {
		return servicos;
	}
	public void setServicos(List<ServicoKitVO> servicos) {
		this.servicos = servicos;
	}
	public List<ProdutoKitVO> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<ProdutoKitVO> produtos) {
		this.produtos = produtos;
	}
	
	
	
}
