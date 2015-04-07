package br.com.min.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Comissao extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ComissaoServico> comissoesServico = new ArrayList<ComissaoServico>();;
	private Double comissaoAuxiliar = 0.0;
	private Double valorRange1 = 0.0;
	private Double valorRange2 = 0.0;
	private Double valorRange3 = 0.0;
	private Double comissaoRange1 = 0.0;
	private Double comissaoRange2 = 0.0;
	private Double comissaoRange3 = 0.0;
	private Double comissaoRange4 = 0.0;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getComissaoAuxiliar() {
		return comissaoAuxiliar;
	}
	public void setComissaoAuxiliar(Double comissaoAuxiliar) {
		this.comissaoAuxiliar = comissaoAuxiliar;
	}
	public Double getValorRange1() {
		return valorRange1;
	}
	public void setValorRange1(Double valorRange1) {
		this.valorRange1 = valorRange1;
	}
	public Double getValorRange2() {
		return valorRange2;
	}
	public void setValorRange2(Double valorRange2) {
		this.valorRange2 = valorRange2;
	}
	public Double getValorRange3() {
		return valorRange3;
	}
	public void setValorRange3(Double valorRange3) {
		this.valorRange3 = valorRange3;
	}
	public Double getComissaoRange1() {
		return comissaoRange1;
	}
	public void setComissaoRange1(Double comissaoRange1) {
		this.comissaoRange1 = comissaoRange1;
	}
	public Double getComissaoRange2() {
		return comissaoRange2;
	}
	public void setComissaoRange2(Double comissaoRange2) {
		this.comissaoRange2 = comissaoRange2;
	}
	public Double getComissaoRange3() {
		return comissaoRange3;
	}
	public void setComissaoRange3(Double comissaoRange3) {
		this.comissaoRange3 = comissaoRange3;
	}
	public Double getComissaoRange4() {
		return comissaoRange4;
	}
	public void setComissaoRange4(Double comissaoRange4) {
		this.comissaoRange4 = comissaoRange4;
	}
	public List<ComissaoServico> getComissoesServico() {
		return comissoesServico;
	}
	public void setComissoesServico(List<ComissaoServico> comissoesServico) {
		this.comissoesServico = comissoesServico;
	}
	@Transient
	public ComissaoServico findComissaoServico(TipoServico tipoServico){
		if(comissoesServico != null){
			for(ComissaoServico comissaoServico : comissoesServico){
				if(tipoServico.equals(comissaoServico.getTipoServico())){
					return comissaoServico;
				}
			}
		}
		return null;
	}
}
