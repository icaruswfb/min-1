package br.com.min.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Kit extends BaseEntity{

	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private Double valor;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Servico> servicos = new ArrayList<>();
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ProdutoQuantidade> produtos = new ArrayList<>();
	
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
	public List<Servico> getServicos() {
		return servicos;
	}
	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}
	@Override
	public String toString() {
		return "Kit [id=" + id + ", nome=" + nome + ", valor=" + valor
				+ ", servicos=" + servicos + "]";
	}
	public List<ProdutoQuantidade> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<ProdutoQuantidade> produtos) {
		this.produtos = produtos;
	}
	
}
