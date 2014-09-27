package br.com.min.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class ServicoProduto {
	
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Servico servico;
	@ManyToMany
	private List<ProdutoQuantidade> produtos = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Servico getServico() {
		return servico;
	}
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	public List<ProdutoQuantidade> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<ProdutoQuantidade> produtos) {
		this.produtos = produtos;
	}
	
	
	
}
