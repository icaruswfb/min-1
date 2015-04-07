package br.com.min.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pagamento extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	private Double valor;
	private Date data;
	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;
	private Integer parcelamento;
	private Integer parcela;
	@ManyToOne
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Comanda comanda;
	@Enumerated(EnumType.STRING)
	private FluxoPagamento fluxoPagamento;
	private String observacao;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	public Comanda getComanda() {
		return comanda;
	}
	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
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
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Integer getParcelamento() {
		return parcelamento;
	}
	public void setParcelamento(Integer parcelamento) {
		this.parcelamento = parcelamento;
	}
	public FluxoPagamento getFluxoPagamento() {
		return fluxoPagamento;
	}
	public void setFluxoPagamento(FluxoPagamento fluxoPagamento) {
		this.fluxoPagamento = fluxoPagamento;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Integer getParcela() {
		return parcela;
	}
	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}
	
	
}
