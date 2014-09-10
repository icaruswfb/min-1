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
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LancamentoProduto> produtosUtilizados = new ArrayList<LancamentoProduto>();
	private Double valor;
	
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
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LancamentoServico other = (LancamentoServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
