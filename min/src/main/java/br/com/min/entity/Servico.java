package br.com.min.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Servico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2817758796548313446L;
	private Long id;
	private String nome;
	private Long duracao;
	private Integer duracaoMinutos;
	private Long tempoAcaoProduto;
	private Integer tempoAcaoProdutoMinutos;
	private Double preco;
	private Boolean comicionado;
	private TipoServico tipoServico;
	
	@Id
	@GeneratedValue
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
	public Long getDuracao() {
		return duracao;
	}
	public void setDuracao(Long duracao) {
		this.duracao = duracao;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	public Integer getDuracaoMinutos() {
		return duracaoMinutos;
	}
	public void setDuracaoMinutos(Integer duracaoMinutos) {
		this.duracaoMinutos = duracaoMinutos;
	}
	public Long getTempoAcaoProduto() {
		return tempoAcaoProduto;
	}
	public void setTempoAcaoProduto(Long tempoAcaoProduto) {
		this.tempoAcaoProduto = tempoAcaoProduto;
	}
	public Integer getTempoAcaoProdutoMinutos() {
		return tempoAcaoProdutoMinutos;
	}
	public void setTempoAcaoProdutoMinutos(Integer tempoAcaoProdutoMinutos) {
		this.tempoAcaoProdutoMinutos = tempoAcaoProdutoMinutos;
	}
	public Boolean getComicionado() {
		return comicionado;
	}
	public void setComicionado(Boolean comicionado) {
		this.comicionado = comicionado;
	}
	@ManyToOne
	public TipoServico getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
}
