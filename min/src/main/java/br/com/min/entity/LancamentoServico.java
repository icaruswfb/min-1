package br.com.min.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class LancamentoServico {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne()
	private Comanda comanda;
	@ManyToOne
	private Pessoa funcionario;
	@ManyToOne
	private Pessoa assistente;
	@ManyToOne
	private Servico servico;
	private Date dataCriacao;
	@OneToMany(cascade=CascadeType.ALL)
	private List<LancamentoProduto> produtosUtilizados = new ArrayList<LancamentoProduto>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Comanda getComanda() {
		return comanda;
	}
	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}
	public Servico getServico() {
		return servico;
	}
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	public List<LancamentoProduto> getProdutosUtilizados() {
		return produtosUtilizados;
	}
	public void setProdutosUtilizados(List<LancamentoProduto> produtosUtilizados) {
		this.produtosUtilizados = produtosUtilizados;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Pessoa getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	public Pessoa getAssistente() {
		return assistente;
	}
	public void setAssistente(Pessoa assistente) {
		this.assistente = assistente;
	}
	
}
