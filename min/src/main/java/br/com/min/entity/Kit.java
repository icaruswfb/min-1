package br.com.min.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Kit implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private Double valor;
	@OneToMany
	private List<ServicoProduto> servicos = new ArrayList<>();
	
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
	public List<ServicoProduto> getServicos() {
		return servicos;
	}
	public void setServicos(List<ServicoProduto> servicos) {
		this.servicos = servicos;
	}
	
}
