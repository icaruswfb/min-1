package br.com.min.entity;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.min.utils.Utils;

@Entity
public class Tarefa {

	@Id
	@GeneratedValue
	private Long id;
	private String descricao;
	private Date dataCriacao;
	private Date dataAgendada;
	private Boolean concluida;
	@ManyToOne
	private Pessoa funcionario;
	@ManyToOne
	private Pessoa cliente;
	@ManyToOne
	private Pessoa criador;
	@Transient
	private String dataAgendadaStr;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Date getDataAgendada() {
		return dataAgendada;
	}
	public void setDataAgendada(Date dataAgendada) {
		this.dataAgendada = dataAgendada;
	}
	public Boolean getConcluida() {
		return concluida;
	}
	public void setConcluida(Boolean concluida) {
		this.concluida = concluida;
	}
	public Pessoa getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	public Pessoa getCliente() {
		return cliente;
	}
	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}
	public Pessoa getCriador() {
		return criador;
	}
	public void setCriador(Pessoa criador) {
		this.criador = criador;
	}
	public String getDataAgendadaStr() {
		return dataAgendadaStr;
	}
	public void setDataAgendadaStr(String dataAgendadaStr) {
		this.dataAgendadaStr = dataAgendadaStr;
		try {
			this.dataAgendada = Utils.dateTimeFormat.parse(dataAgendadaStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
}
