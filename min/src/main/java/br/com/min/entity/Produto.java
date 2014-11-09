package br.com.min.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Produto {

	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private Double custoUnitario;
	private Double precoRevenda;
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidade;
	private Long quantidadeAlerta;
	@Enumerated(EnumType.STRING)
	private SituacaoEstoque situacaoEstoque;
	@Enumerated(EnumType.STRING)
	private CategoriaProduto categoria;
	@ManyToOne
	private TipoServico tipoServico;
	@ManyToOne
	private TipoProduto tipoProduto;
	private Long quantidadePorUnidade;
	
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
	public Double getCustoUnitario() {
		return custoUnitario;
	}
	public void setCustoUnitario(Double custoUnitario) {
		this.custoUnitario = custoUnitario;
	}
	public Double getPrecoRevenda() {
		return precoRevenda;
	}
	public void setPrecoRevenda(Double precoRevenda) {
		this.precoRevenda = precoRevenda;
	}
	public UnidadeMedida getUnidade() {
		return unidade;
	}
	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}
	public Long getQuantidadeAlerta() {
		return quantidadeAlerta;
	}
	public void setQuantidadeAlerta(Long quantidadeAlerta) {
		this.quantidadeAlerta = quantidadeAlerta;
	}
	public SituacaoEstoque getSituacaoEstoque() {
		return situacaoEstoque;
	}
	public void setSituacaoEstoque(SituacaoEstoque situacaoEstoque) {
		this.situacaoEstoque = situacaoEstoque;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public CategoriaProduto getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}
	public TipoServico getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Long getQuantidadePorUnidade() {
		return quantidadePorUnidade;
	}
	public void setQuantidadePorUnidade(Long quantidadePorUnidade) {
		this.quantidadePorUnidade = quantidadePorUnidade;
	}
	
}
