package br.com.min.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LancamentoProduto extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Produto produto;
	private Long quantidadeUtilizada;
	private Date dataCriacao;
	@ManyToOne
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Comanda comanda;
	@ManyToOne
	private Pessoa vendedor;
	private Boolean lancado;
	private Double valor;
	private Boolean revenda;
	private Double custo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Comanda getComanda() {
		return comanda;
	}
	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}
	public Pessoa getVendedor() {
		return vendedor;
	}
	public void setVendedor(Pessoa vendedor) {
		this.vendedor = vendedor;
	}
	public Boolean getLancado() {
		return lancado;
	}
	public void setLancado(Boolean lancado) {
		this.lancado = lancado;
	}
	public Long getQuantidadeUtilizada() {
		return quantidadeUtilizada;
	}
	public void setQuantidadeUtilizada(Long quantidadeUtilizada) {
		this.quantidadeUtilizada = quantidadeUtilizada;
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
		LancamentoProduto other = (LancamentoProduto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Boolean getRevenda() {
		return revenda;
	}
	public void setRevenda(Boolean revenda) {
		this.revenda = revenda;
	}
	public Double getCusto() {
		return custo;
	}
	public void setCusto(Double custo) {
		this.custo = custo;
	}
}
